package com.springbootlibrary.utils;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {
    public static void main(String[] args) {
        String token = new String("Bearer eyJraWQiOiJZTC1sYVQtdl9QNUlHZkt1QTJ4Mmt3cm5GbmZ2OTJ5Slg3TFJyaG5ObGRzIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULnYyQTVlcDU3YjJuZXA0amF4cXJWLUFPWTZLTV9lUnU2RWZvNTliaWMwVWciLCJpc3MiOiJodHRwczovL2Rldi00MjY5MzIxNC5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6ImFwaTovL2RlZmF1bHQiLCJpYXQiOjE2NzY2Njk5NjUsImV4cCI6MTY3NjY3MzU2NSwiY2lkIjoiMG9hOGNpanMxNkQ4TVAzU0Y1ZDciLCJ1aWQiOiIwMHU4Y2ljY3Q2TXFsanQyRjVkNyIsInNjcCI6WyJvcGVuaWQiLCJlbWFpbCIsInByb2ZpbGUiXSwiYXV0aF90aW1lIjoxNjc2NjY5OTY0LCJzdWIiOiJ0ZXN0dXNlckBlbWFpbC5jb20ifQ.Rpwk3IwFEiC_vxe8YmVxfzCulmHBSr477frPCVbGSTN_bkS97gukdf-90KgwBqDtYIF8xSNJDpVyj67laSOCR-x8BaQqY0o3t5uDWL9zKCOdrC72hgGuH-Sayib5td1edoxsavfh6zpH4ppyZdmon3EGT15hQTMBvtfOPBHuY6SQThWKQNndOvEiPFSENK0iZAL52FI1iZzx15k00IvpUWNSzuOWA-M9w5K-VtOCADXZCRUFuzc4C3nicAR81i8pnIY04AosZsr2sRbseURolLS_Bkmq1T1FmtVlQQgrfFZhqT5SGaE4cqihFJ327RmqEbNaaYRYoULec_hTUvsszg");
        System.out.println(payloadJWTExtraction(token, "\"sub\""));
    }

    public static String payloadJWTExtraction(String token, String fieldName) {
        token = token.replace("Bearer ", "");

        //Split into the header, payload and signature base 64 chunks
        String[] chunks = token.split("\\.");

        //Decoder to decode the JWT into information we can understand
        Base64.Decoder decoder = Base64.getUrlDecoder();

        //Decode the second chunk which is the payload
        String payload = new String(decoder.decode(chunks[1]));
        payload = payload.replace("{", "");
        payload = payload.replace("}", "");

        //Split the payload by commas to get the separate entries.
        String[] entries = payload.split(",");

        Map<String, String> map = new HashMap<>();

        for (String entry: entries) {
            //Split the current string using the colon and assign to the array keyValue
            //The first index would be the key. The second index would be the value

            //Handles items in an array where there is no colon
            if(!entry.contains(":")){
                entry+="\":\"";
            }
            String[] keyValue = entry.split(":");
            map.put(keyValue[0], keyValue[1]);
        }

        if(map.containsKey(fieldName)) {
            return map.get(fieldName).replaceAll("\"","");
        } else {
            return null;
        }
    }

}
