package io.dizme;

import com.authlete.sd.SDJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dizme.idp.models.VerificationSessionInfo;

public class Main {
    public static void main(String[] args) throws Exception {
        String jsonResponse = "{\n" +
                "    \"id\": \"36b5f4d0-c1b1-419f-a5e3-8f2590f66bd3\",\n" +
                "    \"presentationDefinition\": {\n" +
                "        \"input_descriptors\": [\n" +
                "            {\n" +
                "                \"id\": \"PID\",\n" +
                "                \"format\": {\n" +
                "                    \"jwt_vc_json\": {\n" +
                "                        \"alg\": [\n" +
                "                            \"EdDSA\"\n" +
                "                        ]\n" +
                "                    }\n" +
                "                },\n" +
                "                \"constraints\": {\n" +
                "                    \"fields\": [\n" +
                "                        {\n" +
                "                            \"path\": [\n" +
                "                                \"$.type\"\n" +
                "                            ],\n" +
                "                            \"filter\": {\n" +
                "                                \"type\": \"string\",\n" +
                "                                \"pattern\": \"PID\"\n" +
                "                            }\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"tokenResponse\": {\n" +
                "        \"vp_token\": \"eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCIsImtpZCI6ImRpZDprZXk6ejZNa29tYTQ3WmFFV3JQN1NBTUQ5ZENtalZQUEx2a2JFd2dqUm5ZVmp5U2ZNTXYzI3o2TWtvbWE0N1phRVdyUDdTQU1EOWRDbWpWUFBMdmtiRXdnalJuWVZqeVNmTU12MyJ9.eyJzdWIiOiJkaWQ6a2V5Ono2TWtvbWE0N1phRVdyUDdTQU1EOWRDbWpWUFBMdmtiRXdnalJuWVZqeVNmTU12MyIsIm5iZiI6MTcxMDg2MDc5MSwiaWF0IjoxNzEwODYwODUxLCJqdGkiOiIxIiwiaXNzIjoiZGlkOmtleTp6Nk1rb21hNDdaYUVXclA3U0FNRDlkQ21qVlBQTHZrYkV3Z2pSbllWanlTZk1NdjMiLCJub25jZSI6IiIsImF1ZCI6Imh0dHBzOi8vdmVyaWZpZXItd2FsdC1hd3MuZGl6bWUuaW8vb3BlbmlkNHZjL3ZlcmlmeSIsInZwIjp7IkBjb250ZXh0IjpbImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIl0sInR5cGUiOlsiVmVyaWZpYWJsZVByZXNlbnRhdGlvbiJdLCJpZCI6IjEiLCJob2xkZXIiOiJkaWQ6a2V5Ono2TWtvbWE0N1phRVdyUDdTQU1EOWRDbWpWUFBMdmtiRXdnalJuWVZqeVNmTU12MyIsInZlcmlmaWFibGVDcmVkZW50aWFsIjpbImV5SmpkSGtpT2lKamNtVmtaVzUwYVdGc0xXTnNZV2x0Y3kxelpYUXJhbk52YmlJc0luUjVjQ0k2SW5aakszTmtMV3AzZENJc0ltRnNaeUk2SWtWa1JGTkJJaXdpYTJsa0lqb2laR2xrT210bGVUcDZOazFyY0UwemVYaE9TR1o2VW1rNVJYYzBhbk4xVGsxeGRuUjBNM2Q2ZDFCaE9URjFURmg2VTFkSU56TmpSemtpZlEuZXlKQVkyOXVkR1Y0ZENJNld5Sm9kSFJ3Y3pvdkwzZDNkeTUzTXk1dmNtY3ZNakF4T0M5amNtVmtaVzUwYVdGc2N5OTJNU0pkTENKMGVYQmxJanBiSWxabGNtbG1hV0ZpYkdWRGNtVmtaVzUwYVdGc0lpd2lVRWxFSWwwc0ltTnlaV1JsYm5ScFlXeFRkV0pxWldOMElqcDdJbDl6WkNJNld5SndRM2RtWlVsTlZuUmFibEp1TkhkTVUyUlpVSGxDVkZOdFJGRm1ZVXhZY0dJME4ycGlTREkwVFdWWklpd2lZMUV4UWxkTlpqVXphemhuUTJwUlVEaFRaSEZvYW01Tk1tZG5ZMDB6VmtORGJGbEJjVE40VkVKRlFTSXNJa2xJTUZnNE1VWlNabGRwU2pOUWFVdFBiRUpsU1hwWVZ6QkpVVTExWmtWb1FsZGFVRkJ0YkhCWGMxRWlMQ0pqTjFKNWVsbzVRVUZMTnpCUWRHRkRPVzk1UWkxelQyNVlhbmt3VmpkWVVGYzFaakp5VVhaeWIyVTBJaXdpYjB0Q01tRm1WRWxhYWxSRFNsaEZVVGx0YTJ0eWEzY3RSMU52Y25WaVJFWlJUVWRJTW10WVIwcG5SU0lzSWxKUlJrUkVXR3hwUmpaelJTMDFNa3BNVVdaSk1tc3pPVlJpYUhaSGNtSkhVMjVhWXpOaGNrTnpOMVVpTENJemJqSndXUzF3TmtkWlJESmZaVnBWVFZGTmQwOWlZMmR2V2kxTVgwZzFhMUV4ZHpsR01YRTJZbVZSSWl3aVdGVXdaVk5KWjAxQ1IxRklVMlUzVGsxeVVtMUtWalJEWWtWTlVETlJMWFY0TUhkR1JIcE1UMXB3TUNJc0lucEdhbmRYTUdnMVp6Rk1hVGxTYkZWUWNrcG9kVXN5TWpsdWNGVTVOMTk2TWt4Sk1WaHdSRFExTlRnaUxDSlVkVVZQTkVoMlRIY3RTRWhYYlZOMVJUTkRObTVQV2s1Wk1UTmxOM0U0YUdFM1ZFWk1WRVV0YnpSWklpd2lTbE5uVlhwSWRFMVNYMlYwWlRsd1EyOVpTRTVPYWtoUE4xSTBUVkZxV21wdVkyRXRXV1pIWmpsQmJ5SXNJaTB4WDJveGNtdFJjRVZ1TUdSU1RGaFlaRFkxZURONWFqRnFNemd3YW0xVmIwSjBOa05rVmw5aVVEZ2lMQ0l3TlhSelduWmphekY2TTFOc1p6bFdSSG80WjJ4RUxYRjVNemREY0cxMU4yMTBjVTQxUlVGTmRHNVZJaXdpV1VaVVIwUk9SMmhNUVhWQ05HWndkV3hRUVVvM1gwbHVZMmhUTjBrM2RVOXNNVVJvUzB4S01sVTJVU0lzSWtGdVdsVmxXR3h2UTFSelVXMVFORkp1Y2psT2JrbFBRVVpZYVdFd2FHMVhXVzk0V0hSNldtcEVOMVVpTENJeldscDFaek14TTFaVlIxaDZXakl0Y3pKUVZUTXlXRkJmYkRaU2IzVXdSMU42ZWxaYVVVVmhhbTVCSWl3aWVHRnRiVU5mV0RWV1VXSXRUV2xyYVdocFFVUldaRFV5TldOWlgyOUllWGhrVGtrMmFXcDFaa0ZVY3lJc0luSlJlRGRNY0hJMlNuZHlURTVoUW1ReVNFaGZUREpRTkZkTWFtRjBTM0pvTldWclVVdHRSWFZyYURBaUxDSTNZM28xU1Roa1FqaFNUVUZGZURSRVZrbG5abVJQUWpCV1VGWlRiVzVDVjA5b2Fqa3lXVGxUWWsxRklpd2lVVTVXVVRaalJsQjRRV0ozZVUwdFZIQm9ka0ZmWmtobVpGZ3RjbFZUYUUxalZIRllSVE5qU1VsbVVTSXNJbWxsYjJsRlRFeHZkMUpuU1dKSVEySmFMWEpSWTNGS2F6aEliV1JVYUdsNFYwZENOa0ZEYm1NNWVuTWlMQ0owTVhkSFJtUm1VMWhvYTNBM1NYTjBkMFY0TkU0eGVrdzNkazFsTm13M1gzUnRlVVJWTW1OMWFXVnZJaXdpUVRGWVN6bDBPWFZGTUcxbWNqSkJNVnBqWlZweE0wOXlXRW8wTkMxRFQzbzNVa2Q1VDNNeGRVTm1aeUpkZlN3aWFXUWlPaUoxY200NmRYVnBaRG81TkdFME9HWXlPUzFoTXpNMkxUUmxOVFV0WWpsaE5DMDJOekE0T0RBME1qTTVZVFFpTENKcGMzTjFaV1FpT2lJeU1ESXhMVEE0TFRNeFZEQXdPakF3T2pBd1dpSXNJbWx6YzNWbGNpSTZleUpwWkNJNkltUnBaRHByWlhrNmVqWk5hM0JOTTNsNFRraG1lbEpwT1VWM05HcHpkVTVOY1haMGRETjNlbmRRWVRreGRVeFllbE5YU0RjelkwYzVJaXdpYVcxaFoyVWlPbnNpYVdRaU9pSm9kSFJ3Y3pvdkwzZDNkeTVwYm1adlkyVnlkQzVwZEM5amIyNTBaVzUwTDNWd2JHOWhaSE12TWpBeU1TOHdPUzlzYjJkdkxuTjJaeUlzSW5SNWNHVWlPaUpKYldGblpTSjlMQ0p1WVcxbElqb2lTVzVtYjJObGNuUWlMQ0pqYjNWdWRISjVJam9pU1ZRaUxDSjBlWEJsSWpvaVVISnZabWxzWlNJc0luVnliQ0k2SW1oMGRIQnpPaTh2ZDNkM0xtbHVabTlqWlhKMExtbDBMeUo5TENKMllXeHBaRVp5YjIwaU9pSXlNREl4TFRBNExUTXhWREF3T2pBd09qQXdXaUlzSW1semMzVmhibU5sUkdGMFpTSTZJakl3TWpRdE1ETXRNRGRVTURrNk5UYzZOREl1TXpZNU1EVTRORGd3V2lKOS5IS3FVMFUzMjE3YWotMldzWmg2R0s2NW1VZ25MdXFvQmotVTBpSmlnbnMyajkyeEhyX0VwUGVSbnA1UHFRWGk3a193dG9QaV9LZDViZjRLS3dDWDRCZ35XeUpwVVVKU1RYRnZZVFpCUjBobWRtOXRkekJJTXpWQklpd2labUZ0YVd4NVgyNWhiV1VpTENKbVlXMXBiSGxmYm1GdFpTSmR-V3lJemRrOVdXVWhJTVhCdWFUZ3paRFZsUlZkUldXWm5JaXdpWjJsMlpXNWZibUZ0WlNJc0ltZHBkbVZ1WDI1aGJXVWlYUX5XeUpDYW1oWVdrbHpkbEJRYWxSYVZVUkNNMlpvWTBsQklpd2lZbWx5ZEdoZlpHRjBaU0lzSW1KcGNuUm9YMlJoZEdVaVhRfld5SXlVSFJ0VGxCUFIxTkRjRGxHZVc5UFdUZFZTMHhCSWl3aVlXZGxYMjkyWlhKZk1UZ2lMQ0poWjJWZmIzWmxjbDh4T0NKZH5XeUp2UkRsV1ExTlBTV0kzV0dOMGMyMXBPRmRKY0hSQklpd2lZV2RsWDI5MlpYSmZUazRpTENKaFoyVmZiM1psY2w5T1RpSmR-V3lKclNGZE5iemQwVEV0VWVsSXhVVlZ1TTJSRlJrSlJJaXdpWVdkbFgybHVYM2xsWVhKeklpd2lZV2RsWDJsdVgzbGxZWEp6SWwwfld5SkRPR1pzWW1vd1dqa3dUM280ZERscWREUkpWWGhSSWl3aVlXZGxYMkpwY25Sb1gzbGxZWElpTENKaFoyVmZZbWx5ZEdoZmVXVmhjaUpkfld5SkRUekZWUWxocGRsZEhjVlJWUkZOVVJXVkZiRWQzSWl3aWRXNXBjWFZsWDJsa0lpd2lkWEp1T25WMWFXUTZOelk1TUROa1pUZ3RNemN5WlMwME0yUXpMV0psTXpndFkyTTVaRFZtTVdVelpEQmxJbDB-V3lJMldqUjZkVVoyWDNaM2IzaE9iek5TWlVGTFowVm5JaXdpWm1GdGFXeDVYMjVoYldWZlltbHlkR2dpTENKbVlXMXBiSGxmYm1GdFpWOWlhWEowYUNKZH5XeUo1U2xGWWJGaE5Xa1JWUlVrMk4xUXhNVmhyYVdSbklpd2laMmwyWlc1ZmJtRnRaVjlpYVhKMGFDSXNJbWRwZG1WdVgyNWhiV1ZmWW1seWRHZ2lYUX5XeUozTFZGM2NWOWZRVlJXZUZSNVRGTnFTMXBHVG5KQklpd2lZbWx5ZEdoZmNHeGhZMlVpTENKaWFYSjBhRjl3YkdGalpTSmR-V3lKUFZHYzRSR0p4WjB0VWVHUjFibUZ3UlV0alkzSjNJaXdpWW1seWRHaGZZMjkxYm5SeWVTSXNJbUpwY25Sb1gyTnZkVzUwY25raVhRfld5SmxlbXRFWWpGNFRXUmliRTFZTVMxMGNWVm1SVzVSSWl3aVltbHlkR2hmYzNSaGRHVWlMQ0ppYVhKMGFGOXpkR0YwWlNKZH5XeUl3VFdRMU4xOXhVWEIzVG01UlQybE5VelJwV1VsM0lpd2lZbWx5ZEdoZlkybDBlU0lzSW1KcGNuUm9YMk5wZEhraVhRfld5SlFYMnBWYTJadGJWTmxOVWxFWkhKeWRESlNlazVuSWl3aWNtVnphV1JsYm5SZllXUmtjbVZ6Y3lJc0luSmxjMmxrWlc1MFgyRmtaSEpsYzNNaVhRfld5SkdWVVpGVFhSclEyeHRRbFp6UkdaWmJsVXRhMnQzSWl3aWNtVnphV1JsYm5SZlkyOTFiblJ5ZVNJc0luSmxjMmxrWlc1MFgyTnZkVzUwY25raVhRfld5Sk1NMDFLV0doemNGTllNRmt3Y214R1ZYTnlSVWxCSWl3aWNtVnphV1JsYm5SZlkybDBlU0lzSW5KbGMybGtaVzUwWDJOcGRIa2lYUX5XeUpVUkhVNVZraG1Na3N6Unkxa1VEZFBVV1pvTWxSbklpd2ljbVZ6YVdSbGJuUmZjM1JoZEdVaUxDSnlaWE5wWkdWdWRGOXpkR0YwWlNKZH5XeUpmTTBSamFIaDJMVWswVGsxSU5IbG1RVm94YTI5Uklpd2ljbVZ6YVdSbGJuUmZjRzl6ZEdGc1gyTnZaR1VpTENKeVpYTnBaR1Z1ZEY5d2IzTjBZV3hmWTI5a1pTSmR-V3lJeVgwRm9WMVpuVUdseU9GZEpXakJoVWpWblRVVjNJaXdpY21WemFXUmxiblJmYzNSeVpXVjBJaXdpY21WemFXUmxiblJmYzNSeVpXVjBJbDB-V3lKaWFWZzBjak5mYVdOMFJGWXdhbkpKY0VVd01FVm5JaXdpY21WemFXUmxiblJmYUc5MWMyVmZiblZ0WW1WeUlpd2ljbVZ6YVdSbGJuUmZhRzkxYzJWZmJuVnRZbVZ5SWwwfld5STBSbUZsUzJkNmMwZ3daWFpqYVRkUVNUSjRURWxuSWl3aVoyVnVaR1Z5SWl3aVoyVnVaR1Z5SWwwfld5SkZXRTh6U1VWNmRtdERkbkZUYmsxb1MzSkhWRFJSSWl3aWJtRjBhVzl1WVd4cGRIa2lMQ0p1WVhScGIyNWhiR2wwZVNKZCJdfX0.cInjLjWRJVYJEgpAX2Zrwylkv3-RwxLT3QcA8Kfk00XsgrgkYrlAOQqC14ycppQSjxS7p0jZj6zx1B9Fh7HICg\",\n" +
                "        \"presentation_submission\": {\n" +
                "            \"id\": \"1\",\n" +
                "            \"definition_id\": \"1\",\n" +
                "            \"descriptor_map\": [\n" +
                "                {\n" +
                "                    \"format\": \"jwt_vp\",\n" +
                "                    \"path\": \"$\",\n" +
                "                    \"path_nested\": {\n" +
                "                        \"format\": \"jwt_vc_json\",\n" +
                "                        \"path\": \"$.verifiableCredential[0]\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"state\": \"36b5f4d0-c1b1-419f-a5e3-8f2590f66bd3\"\n" +
                "    },\n" +
                "    \"verificationResult\": true,\n" +
                "    \"policyResults\": {\n" +
                "        \"results\": [\n" +
                "            {\n" +
                "                \"credential\": \"VerifiablePresentation\",\n" +
                "                \"policies\": [\n" +
                "                    {\n" +
                "                        \"policy\": \"signature\",\n" +
                "                        \"description\": \"Checks a JWT credential by verifying its cryptographic signature using the key referenced by the DID in `iss`.\",\n" +
                "                        \"is_success\": true,\n" +
                "                        \"result\": {\n" +
                "                            \"sub\": \"did:key:z6Mkoma47ZaEWrP7SAMD9dCmjVPPLvkbEwgjRnYVjySfMMv3\",\n" +
                "                            \"nbf\": 1710860791,\n" +
                "                            \"iat\": 1710860851,\n" +
                "                            \"jti\": \"1\",\n" +
                "                            \"iss\": \"did:key:z6Mkoma47ZaEWrP7SAMD9dCmjVPPLvkbEwgjRnYVjySfMMv3\",\n" +
                "                            \"nonce\": \"\",\n" +
                "                            \"aud\": \"https://verifier-walt-aws.dizme.io/openid4vc/verify\",\n" +
                "                            \"vp\": {\n" +
                "                                \"@context\": [\n" +
                "                                    \"https://www.w3.org/2018/credentials/v1\"\n" +
                "                                ],\n" +
                "                                \"type\": [\n" +
                "                                    \"VerifiablePresentation\"\n" +
                "                                ],\n" +
                "                                \"id\": \"1\",\n" +
                "                                \"holder\": \"did:key:z6Mkoma47ZaEWrP7SAMD9dCmjVPPLvkbEwgjRnYVjySfMMv3\",\n" +
                "                                \"verifiableCredential\": [\n" +
                "                                    \"eyJjdHkiOiJjcmVkZW50aWFsLWNsYWltcy1zZXQranNvbiIsInR5cCI6InZjK3NkLWp3dCIsImFsZyI6IkVkRFNBIiwia2lkIjoiZGlkOmtleTp6Nk1rcE0zeXhOSGZ6Umk5RXc0anN1Tk1xdnR0M3d6d1BhOTF1TFh6U1dINzNjRzkifQ.eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSJdLCJ0eXBlIjpbIlZlcmlmaWFibGVDcmVkZW50aWFsIiwiUElEIl0sImNyZWRlbnRpYWxTdWJqZWN0Ijp7Il9zZCI6WyJwQ3dmZUlNVnRablJuNHdMU2RZUHlCVFNtRFFmYUxYcGI0N2piSDI0TWVZIiwiY1ExQldNZjUzazhnQ2pRUDhTZHFoam5NMmdnY00zVkNDbFlBcTN4VEJFQSIsIklIMFg4MUZSZldpSjNQaUtPbEJlSXpYVzBJUU11ZkVoQldaUFBtbHBXc1EiLCJjN1J5elo5QUFLNzBQdGFDOW95Qi1zT25YankwVjdYUFc1ZjJyUXZyb2U0Iiwib0tCMmFmVElaalRDSlhFUTlta2tya3ctR1NvcnViREZRTUdIMmtYR0pnRSIsIlJRRkREWGxpRjZzRS01MkpMUWZJMmszOVRiaHZHcmJHU25aYzNhckNzN1UiLCIzbjJwWS1wNkdZRDJfZVpVTVFNd09iY2dvWi1MX0g1a1ExdzlGMXE2YmVRIiwiWFUwZVNJZ01CR1FIU2U3Tk1yUm1KVjRDYkVNUDNRLXV4MHdGRHpMT1pwMCIsInpGandXMGg1ZzFMaTlSbFVQckpodUsyMjlucFU5N196MkxJMVhwRDQ1NTgiLCJUdUVPNEh2THctSEhXbVN1RTNDNm5PWk5ZMTNlN3E4aGE3VEZMVEUtbzRZIiwiSlNnVXpIdE1SX2V0ZTlwQ29ZSE5OakhPN1I0TVFqWmpuY2EtWWZHZjlBbyIsIi0xX2oxcmtRcEVuMGRSTFhYZDY1eDN5ajFqMzgwam1Vb0J0NkNkVl9iUDgiLCIwNXRzWnZjazF6M1NsZzlWRHo4Z2xELXF5MzdDcG11N210cU41RUFNdG5VIiwiWUZUR0ROR2hMQXVCNGZwdWxQQUo3X0luY2hTN0k3dU9sMURoS0xKMlU2USIsIkFuWlVlWGxvQ1RzUW1QNFJucjlObklPQUZYaWEwaG1XWW94WHR6WmpEN1UiLCIzWlp1ZzMxM1ZVR1h6WjItczJQVTMyWFBfbDZSb3UwR1N6elZaUUVham5BIiwieGFtbUNfWDVWUWItTWlraWhpQURWZDUyNWNZX29IeXhkTkk2aWp1ZkFUcyIsInJReDdMcHI2SndyTE5hQmQySEhfTDJQNFdMamF0S3JoNWVrUUttRXVraDAiLCI3Y3o1SThkQjhSTUFFeDREVklnZmRPQjBWUFZTbW5CV09oajkyWTlTYk1FIiwiUU5WUTZjRlB4QWJ3eU0tVHBodkFfZkhmZFgtclVTaE1jVHFYRTNjSUlmUSIsImllb2lFTExvd1JnSWJIQ2JaLXJRY3FKazhIbWRUaGl4V0dCNkFDbmM5enMiLCJ0MXdHRmRmU1hoa3A3SXN0d0V4NE4xekw3dk1lNmw3X3RteURVMmN1aWVvIiwiQTFYSzl0OXVFMG1mcjJBMVpjZVpxM09yWEo0NC1DT3o3Ukd5T3MxdUNmZyJdfSwiaWQiOiJ1cm46dXVpZDo5NGE0OGYyOS1hMzM2LTRlNTUtYjlhNC02NzA4ODA0MjM5YTQiLCJpc3N1ZWQiOiIyMDIxLTA4LTMxVDAwOjAwOjAwWiIsImlzc3VlciI6eyJpZCI6ImRpZDprZXk6ejZNa3BNM3l4TkhmelJpOUV3NGpzdU5NcXZ0dDN3endQYTkxdUxYelNXSDczY0c5IiwiaW1hZ2UiOnsiaWQiOiJodHRwczovL3d3dy5pbmZvY2VydC5pdC9jb250ZW50L3VwbG9hZHMvMjAyMS8wOS9sb2dvLnN2ZyIsInR5cGUiOiJJbWFnZSJ9LCJuYW1lIjoiSW5mb2NlcnQiLCJjb3VudHJ5IjoiSVQiLCJ0eXBlIjoiUHJvZmlsZSIsInVybCI6Imh0dHBzOi8vd3d3LmluZm9jZXJ0Lml0LyJ9LCJ2YWxpZEZyb20iOiIyMDIxLTA4LTMxVDAwOjAwOjAwWiIsImlzc3VhbmNlRGF0ZSI6IjIwMjQtMDMtMDdUMDk6NTc6NDIuMzY5MDU4NDgwWiJ9.HKqU0U3217aj-2WsZh6GK65mUgnLuqoBj-U0iJigns2j92xHr_EpPeRnp5PqQXi7k_wtoPi_Kd5bf4KKwCX4Bg~WyJpUUJSTXFvYTZBR0hmdm9tdzBIMzVBIiwiZmFtaWx5X25hbWUiLCJmYW1pbHlfbmFtZSJd~WyIzdk9WWUhIMXBuaTgzZDVlRVdRWWZnIiwiZ2l2ZW5fbmFtZSIsImdpdmVuX25hbWUiXQ~WyJCamhYWklzdlBQalRaVURCM2ZoY0lBIiwiYmlydGhfZGF0ZSIsImJpcnRoX2RhdGUiXQ~WyIyUHRtTlBPR1NDcDlGeW9PWTdVS0xBIiwiYWdlX292ZXJfMTgiLCJhZ2Vfb3Zlcl8xOCJd~WyJvRDlWQ1NPSWI3WGN0c21pOFdJcHRBIiwiYWdlX292ZXJfTk4iLCJhZ2Vfb3Zlcl9OTiJd~WyJrSFdNbzd0TEtUelIxUVVuM2RFRkJRIiwiYWdlX2luX3llYXJzIiwiYWdlX2luX3llYXJzIl0~WyJDOGZsYmowWjkwT3o4dDlqdDRJVXhRIiwiYWdlX2JpcnRoX3llYXIiLCJhZ2VfYmlydGhfeWVhciJd~WyJDTzFVQlhpdldHcVRVRFNURWVFbEd3IiwidW5pcXVlX2lkIiwidXJuOnV1aWQ6NzY5MDNkZTgtMzcyZS00M2QzLWJlMzgtY2M5ZDVmMWUzZDBlIl0~WyI2WjR6dUZ2X3Z3b3hObzNSZUFLZ0VnIiwiZmFtaWx5X25hbWVfYmlydGgiLCJmYW1pbHlfbmFtZV9iaXJ0aCJd~WyJ5SlFYbFhNWkRVRUk2N1QxMVhraWRnIiwiZ2l2ZW5fbmFtZV9iaXJ0aCIsImdpdmVuX25hbWVfYmlydGgiXQ~WyJ3LVF3cV9fQVRWeFR5TFNqS1pGTnJBIiwiYmlydGhfcGxhY2UiLCJiaXJ0aF9wbGFjZSJd~WyJPVGc4RGJxZ0tUeGR1bmFwRUtjY3J3IiwiYmlydGhfY291bnRyeSIsImJpcnRoX2NvdW50cnkiXQ~WyJlemtEYjF4TWRibE1YMS10cVVmRW5RIiwiYmlydGhfc3RhdGUiLCJiaXJ0aF9zdGF0ZSJd~WyIwTWQ1N19xUXB3Tm5RT2lNUzRpWUl3IiwiYmlydGhfY2l0eSIsImJpcnRoX2NpdHkiXQ~WyJQX2pVa2ZtbVNlNUlEZHJydDJSek5nIiwicmVzaWRlbnRfYWRkcmVzcyIsInJlc2lkZW50X2FkZHJlc3MiXQ~WyJGVUZFTXRrQ2xtQlZzRGZZblUta2t3IiwicmVzaWRlbnRfY291bnRyeSIsInJlc2lkZW50X2NvdW50cnkiXQ~WyJMM01KWGhzcFNYMFkwcmxGVXNyRUlBIiwicmVzaWRlbnRfY2l0eSIsInJlc2lkZW50X2NpdHkiXQ~WyJURHU5VkhmMkszRy1kUDdPUWZoMlRnIiwicmVzaWRlbnRfc3RhdGUiLCJyZXNpZGVudF9zdGF0ZSJd~WyJfM0RjaHh2LUk0Tk1INHlmQVoxa29RIiwicmVzaWRlbnRfcG9zdGFsX2NvZGUiLCJyZXNpZGVudF9wb3N0YWxfY29kZSJd~WyIyX0FoV1ZnUGlyOFdJWjBhUjVnTUV3IiwicmVzaWRlbnRfc3RyZWV0IiwicmVzaWRlbnRfc3RyZWV0Il0~WyJiaVg0cjNfaWN0RFYwanJJcEUwMEVnIiwicmVzaWRlbnRfaG91c2VfbnVtYmVyIiwicmVzaWRlbnRfaG91c2VfbnVtYmVyIl0~WyI0RmFlS2d6c0gwZXZjaTdQSTJ4TElnIiwiZ2VuZGVyIiwiZ2VuZGVyIl0~WyJFWE8zSUV6dmtDdnFTbk1oS3JHVDRRIiwibmF0aW9uYWxpdHkiLCJuYXRpb25hbGl0eSJd\"\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"credential\": \"PID\",\n" +
                "                \"policies\": [\n" +
                "                    {\n" +
                "                        \"policy\": \"signature\",\n" +
                "                        \"description\": \"Checks a JWT credential by verifying its cryptographic signature using the key referenced by the DID in `iss`.\",\n" +
                "                        \"is_success\": true,\n" +
                "                        \"result\": {\n" +
                "                            \"@context\": [\n" +
                "                                \"https://www.w3.org/2018/credentials/v1\"\n" +
                "                            ],\n" +
                "                            \"type\": [\n" +
                "                                \"VerifiableCredential\",\n" +
                "                                \"PID\"\n" +
                "                            ],\n" +
                "                            \"credentialSubject\": {\n" +
                "                                \"_sd\": [\n" +
                "                                    \"pCwfeIMVtZnRn4wLSdYPyBTSmDQfaLXpb47jbH24MeY\",\n" +
                "                                    \"cQ1BWMf53k8gCjQP8SdqhjnM2ggcM3VCClYAq3xTBEA\",\n" +
                "                                    \"IH0X81FRfWiJ3PiKOlBeIzXW0IQMufEhBWZPPmlpWsQ\",\n" +
                "                                    \"c7RyzZ9AAK70PtaC9oyB-sOnXjy0V7XPW5f2rQvroe4\",\n" +
                "                                    \"oKB2afTIZjTCJXEQ9mkkrkw-GSorubDFQMGH2kXGJgE\",\n" +
                "                                    \"RQFDDXliF6sE-52JLQfI2k39TbhvGrbGSnZc3arCs7U\",\n" +
                "                                    \"3n2pY-p6GYD2_eZUMQMwObcgoZ-L_H5kQ1w9F1q6beQ\",\n" +
                "                                    \"XU0eSIgMBGQHSe7NMrRmJV4CbEMP3Q-ux0wFDzLOZp0\",\n" +
                "                                    \"zFjwW0h5g1Li9RlUPrJhuK229npU97_z2LI1XpD4558\",\n" +
                "                                    \"TuEO4HvLw-HHWmSuE3C6nOZNY13e7q8ha7TFLTE-o4Y\",\n" +
                "                                    \"JSgUzHtMR_ete9pCoYHNNjHO7R4MQjZjnca-YfGf9Ao\",\n" +
                "                                    \"-1_j1rkQpEn0dRLXXd65x3yj1j380jmUoBt6CdV_bP8\",\n" +
                "                                    \"05tsZvck1z3Slg9VDz8glD-qy37Cpmu7mtqN5EAMtnU\",\n" +
                "                                    \"YFTGDNGhLAuB4fpulPAJ7_InchS7I7uOl1DhKLJ2U6Q\",\n" +
                "                                    \"AnZUeXloCTsQmP4Rnr9NnIOAFXia0hmWYoxXtzZjD7U\",\n" +
                "                                    \"3ZZug313VUGXzZ2-s2PU32XP_l6Rou0GSzzVZQEajnA\",\n" +
                "                                    \"xammC_X5VQb-MikihiADVd525cY_oHyxdNI6ijufATs\",\n" +
                "                                    \"rQx7Lpr6JwrLNaBd2HH_L2P4WLjatKrh5ekQKmEukh0\",\n" +
                "                                    \"7cz5I8dB8RMAEx4DVIgfdOB0VPVSmnBWOhj92Y9SbME\",\n" +
                "                                    \"QNVQ6cFPxAbwyM-TphvA_fHfdX-rUShMcTqXE3cIIfQ\",\n" +
                "                                    \"ieoiELLowRgIbHCbZ-rQcqJk8HmdThixWGB6ACnc9zs\",\n" +
                "                                    \"t1wGFdfSXhkp7IstwEx4N1zL7vMe6l7_tmyDU2cuieo\",\n" +
                "                                    \"A1XK9t9uE0mfr2A1ZceZq3OrXJ44-COz7RGyOs1uCfg\"\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            \"id\": \"urn:uuid:94a48f29-a336-4e55-b9a4-6708804239a4\",\n" +
                "                            \"issued\": \"2021-08-31T00:00:00Z\",\n" +
                "                            \"issuer\": {\n" +
                "                                \"id\": \"did:key:z6MkpM3yxNHfzRi9Ew4jsuNMqvtt3wzwPa91uLXzSWH73cG9\",\n" +
                "                                \"image\": {\n" +
                "                                    \"id\": \"https://www.infocert.it/content/uploads/2021/09/logo.svg\",\n" +
                "                                    \"type\": \"Image\"\n" +
                "                                },\n" +
                "                                \"name\": \"Infocert\",\n" +
                "                                \"country\": \"IT\",\n" +
                "                                \"type\": \"Profile\",\n" +
                "                                \"url\": \"https://www.infocert.it/\"\n" +
                "                            },\n" +
                "                            \"validFrom\": \"2021-08-31T00:00:00Z\",\n" +
                "                            \"issuanceDate\": \"2024-03-07T09:57:42.369058480Z\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"success\": true,\n" +
                "        \"time\": \"0.0155s\",\n" +
                "        \"policies_run\": 2,\n" +
                "        \"policies_failed\": 0,\n" +
                "        \"policies_succeeded\": 2\n" +
                "    }\n" +
                "}"; // Your JSON response string here

        ObjectMapper objectMapper = new ObjectMapper();
        VerificationSessionInfo responseObject = objectMapper.readValue(jsonResponse, VerificationSessionInfo.class);

        // Now you can access the fields of responseObject
        System.out.println("ID: " + responseObject.id);
        System.out.println("Presentation Definition: " + responseObject.presentationDefinition);
        System.out.println("Token Response: " + responseObject.tokenResponse);
        System.out.println("Verification Result: " + responseObject.verificationResult);

        System.out.println("VPToken: " + responseObject.tokenResponse.vpToken);
        String verifiableCredential = responseObject.policyResults.results.get(0).policies.get(0).result.vp.verifiableCredentials.get(0);
        System.out.println("Verifiable Credential: " + verifiableCredential);

        SDJWT sdJwt = SDJWT.parse(verifiableCredential);
        sdJwt.getDisclosures().forEach(disclosure -> {
            System.out.println("Disclosure: " + disclosure);
            System.out.println("Claim name: " + disclosure.getClaimName());
            System.out.println("Claim value: " + disclosure.getClaimValue());
        });

    }
}
