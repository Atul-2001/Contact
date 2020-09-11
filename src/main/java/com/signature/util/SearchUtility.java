package com.signature.util;

import com.signature.ui.Main;

public class SearchUtility {

    public static void search(String query) {
//        System.out.println("Query : " + query.replaceAll("\\s+", " ").replaceAll("\\p{Punct}", ""));
        String formattedQuery = query.replaceAll("\\s+", " ").replaceAll("\\p{Punct}", "").trim();
        Main.controller.filteredContacts.setPredicate(contact -> {
            if (!contact.getFirstName().isEmpty()) {
                String firstName = contact.getFirstName();
                String lastName = contact.getLastName();
                if (lastName == null) {
                    lastName = "";
                }
                String fullName = firstName.concat(lastName.isEmpty() ? "" : " " + lastName);

                int len = fullName.length();
                int strLen = formattedQuery.length();

                if (strLen <= len) {
                    len = strLen;
                }

                if (fullName.substring(0, len).compareToIgnoreCase(formattedQuery) == 0) {
//                    System.out.println("FN Pass : " + fullName.substring(0, len));
                    return true;
                }/* else {
                    System.out.println("FN Fail : " + fullName.substring(0, len));
                }*/
            }

            if (contact.getLastName() != null) {
                if (!contact.getLastName().isEmpty()) {
                    String lastName = contact.getLastName();
                    String firstName = contact.getFirstName();
                    String rFullName = lastName + " " + firstName;
                    int len = rFullName.length();
                    int strLen = formattedQuery.length();

                    if (strLen <= len) {
                        len = strLen;
                    }

                    if (rFullName.substring(0, len).compareToIgnoreCase(formattedQuery) == 0) {
//                        System.out.println("LN Pass : " + rFullName.substring(0, len));
                        return true;
                    }/* else {
                        System.out.println("LN Fail : " + rFullName.substring(0, len));
                    }*/
                }
            }

            if (contact.getPhoneNumber() != null) {
                if (!contact.getPhoneNumber().isEmpty()) {
                    String phoneNumber = contact.getPhoneNumber();
                    int len = phoneNumber.length();
                    int strLen = formattedQuery.length();

                    if (strLen <= len) {
                        len = strLen;
                    }

                    if (phoneNumber.substring(0, len).compareToIgnoreCase(formattedQuery) == 0) {
//                        System.out.println("PN Pass : " + phoneNumber.substring(0, len));
                        return true;
                    }/* else {
                        System.out.println("PN Fail : " + phoneNumber.substring(0, len));
                    }*/
                }
            }

            if (contact.getEmail() != null) {
                if (!contact.getEmail().isEmpty()) {
                    String email = contact.getEmail();
                    int len = email.length();
                    int strLen = formattedQuery.length();

                    if (strLen <= len) {
                        len = strLen;
                    }

                    return email.substring(0, len).compareToIgnoreCase(formattedQuery) == 0;
                }
            }
            return false;
        });
    }
}
