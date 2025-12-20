package org.backend.controller;


import org.backend.util.LimitedInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/api/pdf")
public class PdfStreamingController {

    @Value("${pdf.storage.base-path}")
    private String pdfBasePath;

    private static final long MAX_CHUNK_SIZE = 1024 * 1024; // 1MB safety limit

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> streamPdf(
            @PathVariable String filename,
            @RequestHeader(value = "Range", required = false) String range
    ) throws IOException {
        // ⛔ simple sanitization
        filename = filename.replace("..", "");
        File file = new File(pdfBasePath + File.separator + filename);


        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        long fileLength = file.length();

        // =========================
        // NO RANGE → full response
        // =========================
        if (range == null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(fileLength)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .body(new InputStreamResource(new FileInputStream(file)));
        }

        // =========================
        // RANGE REQUEST
        // Example: bytes=0-102399
        // =========================
        String[] ranges = range.replace("bytes=", "").split("-");
        long start = Long.parseLong(ranges[0]);

        long end = ranges.length > 1 && !ranges[1].isEmpty()
                ? Long.parseLong(ranges[1])
                : fileLength - 1;

        // Safety limit (best practice)
        end = Math.min(end, start + MAX_CHUNK_SIZE - 1);

        if (start >= fileLength) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileLength)
                    .build();
        }

        long contentLength = end - start + 1;

        InputStream inputStream = new FileInputStream(file);
        inputStream.skip(start);

        InputStreamResource resource = new InputStreamResource(
                new LimitedInputStream(inputStream, contentLength)
        );

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE,
                        "bytes " + start + "-" + end + "/" + fileLength)
                .contentLength(contentLength)
                .body(resource);
    }
}
