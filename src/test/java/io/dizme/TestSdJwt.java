package io.dizme;

import com.authlete.sd.Disclosure;
import com.authlete.sd.SDJWT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestSdJwt {
    public static void main(String[] args) {
        String sdJwtDue = "eyJjdHkiOiJjcmVkZW50aWFsLWNsYWltcy1zZXQranNvbiIsInR5cCI6InZjK3NkLWp3dCIsImFsZyI6IkVkRFNBIiwia2lkIjoiZGlkOmtleTp6Nk1rcE0zeXhOSGZ6Umk5RXc0anN1Tk1xdnR0M3d6d1BhOTF1TFh6U1dINzNjRzkifQ.eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSJdLCJ0eXBlIjpbIlZlcmlmaWFibGVDcmVkZW50aWFsIiwiUElEIl0sImNyZWRlbnRpYWxTdWJqZWN0Ijp7Il9zZCI6WyJwQ3dmZUlNVnRablJuNHdMU2RZUHlCVFNtRFFmYUxYcGI0N2piSDI0TWVZIiwiY1ExQldNZjUzazhnQ2pRUDhTZHFoam5NMmdnY00zVkNDbFlBcTN4VEJFQSIsIklIMFg4MUZSZldpSjNQaUtPbEJlSXpYVzBJUU11ZkVoQldaUFBtbHBXc1EiLCJjN1J5elo5QUFLNzBQdGFDOW95Qi1zT25YankwVjdYUFc1ZjJyUXZyb2U0Iiwib0tCMmFmVElaalRDSlhFUTlta2tya3ctR1NvcnViREZRTUdIMmtYR0pnRSIsIlJRRkREWGxpRjZzRS01MkpMUWZJMmszOVRiaHZHcmJHU25aYzNhckNzN1UiLCIzbjJwWS1wNkdZRDJfZVpVTVFNd09iY2dvWi1MX0g1a1ExdzlGMXE2YmVRIiwiWFUwZVNJZ01CR1FIU2U3Tk1yUm1KVjRDYkVNUDNRLXV4MHdGRHpMT1pwMCIsInpGandXMGg1ZzFMaTlSbFVQckpodUsyMjlucFU5N196MkxJMVhwRDQ1NTgiLCJUdUVPNEh2THctSEhXbVN1RTNDNm5PWk5ZMTNlN3E4aGE3VEZMVEUtbzRZIiwiSlNnVXpIdE1SX2V0ZTlwQ29ZSE5OakhPN1I0TVFqWmpuY2EtWWZHZjlBbyIsIi0xX2oxcmtRcEVuMGRSTFhYZDY1eDN5ajFqMzgwam1Vb0J0NkNkVl9iUDgiLCIwNXRzWnZjazF6M1NsZzlWRHo4Z2xELXF5MzdDcG11N210cU41RUFNdG5VIiwiWUZUR0ROR2hMQXVCNGZwdWxQQUo3X0luY2hTN0k3dU9sMURoS0xKMlU2USIsIkFuWlVlWGxvQ1RzUW1QNFJucjlObklPQUZYaWEwaG1XWW94WHR6WmpEN1UiLCIzWlp1ZzMxM1ZVR1h6WjItczJQVTMyWFBfbDZSb3UwR1N6elZaUUVham5BIiwieGFtbUNfWDVWUWItTWlraWhpQURWZDUyNWNZX29IeXhkTkk2aWp1ZkFUcyIsInJReDdMcHI2SndyTE5hQmQySEhfTDJQNFdMamF0S3JoNWVrUUttRXVraDAiLCI3Y3o1SThkQjhSTUFFeDREVklnZmRPQjBWUFZTbW5CV09oajkyWTlTYk1FIiwiUU5WUTZjRlB4QWJ3eU0tVHBodkFfZkhmZFgtclVTaE1jVHFYRTNjSUlmUSIsImllb2lFTExvd1JnSWJIQ2JaLXJRY3FKazhIbWRUaGl4V0dCNkFDbmM5enMiLCJ0MXdHRmRmU1hoa3A3SXN0d0V4NE4xekw3dk1lNmw3X3RteURVMmN1aWVvIiwiQTFYSzl0OXVFMG1mcjJBMVpjZVpxM09yWEo0NC1DT3o3Ukd5T3MxdUNmZyJdfSwiaWQiOiJ1cm46dXVpZDo5NGE0OGYyOS1hMzM2LTRlNTUtYjlhNC02NzA4ODA0MjM5YTQiLCJpc3N1ZWQiOiIyMDIxLTA4LTMxVDAwOjAwOjAwWiIsImlzc3VlciI6eyJpZCI6ImRpZDprZXk6ejZNa3BNM3l4TkhmelJpOUV3NGpzdU5NcXZ0dDN3endQYTkxdUxYelNXSDczY0c5IiwiaW1hZ2UiOnsiaWQiOiJodHRwczovL3d3dy5pbmZvY2VydC5pdC9jb250ZW50L3VwbG9hZHMvMjAyMS8wOS9sb2dvLnN2ZyIsInR5cGUiOiJJbWFnZSJ9LCJuYW1lIjoiSW5mb2NlcnQiLCJjb3VudHJ5IjoiSVQiLCJ0eXBlIjoiUHJvZmlsZSIsInVybCI6Imh0dHBzOi8vd3d3LmluZm9jZXJ0Lml0LyJ9LCJ2YWxpZEZyb20iOiIyMDIxLTA4LTMxVDAwOjAwOjAwWiIsImlzc3VhbmNlRGF0ZSI6IjIwMjQtMDMtMDdUMDk6NTc6NDIuMzY5MDU4NDgwWiJ9.HKqU0U3217aj-2WsZh6GK65mUgnLuqoBj-U0iJigns2j92xHr_EpPeRnp5PqQXi7k_wtoPi_Kd5bf4KKwCX4Bg~WyIyUHRtTlBPR1NDcDlGeW9PWTdVS0xBIiwiYWdlX292ZXJfMTgiLCJhZ2Vfb3Zlcl8xOCJd~WyJFWE8zSUV6dmtDdnFTbk1oS3JHVDRRIiwibmF0aW9uYWxpdHkiLCJuYXRpb25hbGl0eSJd~WyJpUUJSTXFvYTZBR0hmdm9tdzBIMzVBIiwiZmFtaWx5X25hbWUiLCJmYW1pbHlfbmFtZSJd";
        SDJWT sdJwt = parseFixed(sdJwtDue);

        sdJwt.getDisclosures().forEach(disclosure -> {
            System.out.println("Disclosure: " + disclosure);
        });

    }
    public static SDJWT parseFixed(String input) {
        if (input == null) {
            return null;
        } else {
            String[] elements = input.split("~", -1);
            System.out.println(elements.length);
            int lastIndex = elements.length - 1;

            for(int i = 0; i < lastIndex; ++i) {
                if (elements[i].isEmpty()) {
                    throw new IllegalArgumentException("The SD-JWT is malformed.");
                }
            }

            if (elements.length < 2) {
                throw new IllegalArgumentException("The SD-JWT is malformed.");
            } else {
                String credentialJwt = elements[0];
                String bindingJwt = input.endsWith("~") ? null : elements[lastIndex];

                List disclosures;
                try {
                    disclosures = (List) Arrays.asList(elements).subList(1, elements.length).stream().map(Disclosure::parse).collect(Collectors.toList());
                } catch (Exception var7) {
                    throw new IllegalArgumentException("Failed to parse disclosures.", var7);
                }

                return new SDJWT(credentialJwt, disclosures, bindingJwt);
            }
        }
    }
}
