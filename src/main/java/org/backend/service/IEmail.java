package org.backend.service;

public interface IEmail {
    void send(String to, String email);
    String buildEmail(String name, String link);
}