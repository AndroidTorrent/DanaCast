package com.sferadev.danacast.providers;

import org.jasypt.util.text.BasicTextEncryptor;

public class SpliveApp {
    private void main() {
        // Lists: http://pastebin.com/ELJcaJhb
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("c6ka74t4b2dv");
        encryptor.decrypt("CSo89epohd25+1KsO6MsYRRHmM1nLoasZi226iSH5L6CjLWZ6CKzl+Alo8Tt/5UxSpY1GD9B8M+NOGguQwd4I10eK006EHpbHqd6yrNyKytaNLcWNIJJlkSVWZQNgqhl");
    }
}
