package com.signature.model;

import com.signature.util.ImageReadWrite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private static final DataSource instance = new DataSource();

    private final String DB_NAME = "contact.db";
    private final String DATA_DIRECTORY = System.getProperty("user.home") + File.separator + ".contactData";
    private final String CONNECTION_URL = "jdbc:sqlite:" + DATA_DIRECTORY + File.separator + "database" + File.separator + DB_NAME;

    private final String TABLE_CONTACTS = "contacts";
    private final String TABLE_MORE_INFO = "moreInfo";
    private final String TABLE_EXTRA_INFO = "extraInfo";
    private final String COL_PROFILE = "profile";
    private final String COL_FIRST_NAME = "first_name";
    private final String COL_LAST_NAME = "last_name";
    private final String COL_PHONE_NUMBER = "phone_number";
    private final String COL_PHONE_TYPE = "phone_type";
    private final String COL_EMAIL = "email";
    private final String COL_EMAIL_TYPE = "email_type";
    private final String COL_ADDRESS = "address";
    private final String COL_COMPANY = "company";
    private final String COL_WEBSITE = "website";
    private final String COL_DATE = "Date";
    private final String COL_DATE_TYPE = "date_type";
    private final String COL_RELATION = "relation";
    private final String COL_RELATION_TYPE = "relation_type";
    private final String COL_SIP = "sip";
    private final String COL_NOTES = "notes";
    private final String COL_IS_FAVOURITE = "is_favourite";
    private final String CONTACTS_COL_ID = "_id";
    private final String MORE_INFO_COL_ID = "_id";
    private final String EXTRA_INFO_COL_ID = "_id";

    //CREATE TABLE IF NOT EXISTS contacts (first_name TEXT PRIMARY KEY, last_name TEXT, phone_number TEXT NOT NULL, phone_type TEXT, profile TEXT, is_favourite TEXT NOT NULL default 'F');
    private final String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " (" + CONTACTS_COL_ID + " INTEGER PRIMARY KEY, " + COL_FIRST_NAME + " TEXT NOT NULL, " + COL_LAST_NAME + " TEXT, " + COL_PHONE_NUMBER + " TEXT NOT NULL, " + COL_PHONE_TYPE + " TEXT, " + COL_PROFILE + " BLOB, " + COL_IS_FAVOURITE + " TEXT DEFAULT 'F')";

    //CREATE TABLE IF NOT EXISTS moreInfo (_id INTEGER PRIMARY KEY, email TEXT, email_type TEXT, address TEXT, company TEXT, Date date, date_type TEXT);
    private final String CREATE_TABLE_MORE_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_MORE_INFO + " (" + MORE_INFO_COL_ID + " INTEGER PRIMARY KEY, " + COL_EMAIL + " TEXT, " + COL_EMAIL_TYPE + " TEXT, " + COL_ADDRESS + " TEXT, " + COL_COMPANY + " TEXT, " + COL_DATE + " date, " + COL_DATE_TYPE + " TEXT)";

    //CREATE TABLE IF NOT EXISTS extraInfo (_id INTEGER PRIMARY KEY, website TEXT, relation TEXT, relation_type TEXT, sip TEXT, notes TEXT);
    private final String CREATE_TABLE_EXTRA_INFO = "CREATE TABLE IF NOT EXISTS " + TABLE_EXTRA_INFO + " (" + EXTRA_INFO_COL_ID + " INTEGER PRIMARY KEY, " + COL_WEBSITE + " TEXT, " + COL_RELATION + " TEXT, " + COL_RELATION_TYPE + " TEXT, " + COL_SIP + " TEXT, " + COL_NOTES + " TEXT)";

    private final String GET_CONTACTS = "SELECT * FROM " + TABLE_CONTACTS;
    private final String GET_MORE_INFO = "SELECT * FROM " + TABLE_MORE_INFO + " WHERE " + MORE_INFO_COL_ID + " = ?";
    private final String GET_EXTRA_INFO = "SELECT * FROM " + TABLE_EXTRA_INFO + " WHERE " + EXTRA_INFO_COL_ID + " = ?";

    private final String SEARCH_CONTACT = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + COL_FIRST_NAME + " = ? AND " + COL_LAST_NAME + " = ?";
    private final String ADD_CONTACT = "INSERT INTO " + TABLE_CONTACTS + "(" + COL_FIRST_NAME + ", " + COL_LAST_NAME + ", " + COL_PHONE_NUMBER + ", " + COL_PHONE_TYPE + ", " + COL_PROFILE + ", " + COL_IS_FAVOURITE + ")" + " VALUES (?, ?, ?, ?, ?, ?)";
    private final String ADD_MORE_INFO = "INSERT INTO " + TABLE_MORE_INFO + " VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String ADD_EXTRA_INFO = "INSERT INTO " + TABLE_EXTRA_INFO + " VALUES (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_CONTACTS = "UPDATE " + TABLE_CONTACTS + " SET " + COL_FIRST_NAME + " = ?, " + COL_LAST_NAME + " = ?, " + COL_PHONE_NUMBER + " = ?, " + COL_PHONE_TYPE + " = ?, " + COL_PROFILE + " = ?, " + COL_IS_FAVOURITE + " = ? WHERE " + CONTACTS_COL_ID + " = ?";
    private final String UPDATE_MORE_FIELDS = "UPDATE " + TABLE_MORE_INFO + " SET " + COL_EMAIL + " = ?, " + COL_EMAIL_TYPE + " = ?, " + COL_ADDRESS + " = ?, " + COL_COMPANY + " = ?, " + COL_DATE + " = ?, " + COL_DATE_TYPE + " = ? WHERE " + MORE_INFO_COL_ID + " = ?";
    private final String UPDATE_EXTRA_FIELDS = "UPDATE " + TABLE_EXTRA_INFO + " SET " + COL_WEBSITE + " = ?, " + COL_RELATION + " = ?, " + COL_RELATION_TYPE + " = ?, " + COL_SIP + " = ?, " + COL_NOTES + " = ? WHERE " + EXTRA_INFO_COL_ID + " = ?";
    private final String DELETE_CONTACT = "DELETE FROM " + TABLE_CONTACTS + " WHERE " + CONTACTS_COL_ID + " = ?";
    private final String DELETE_MORE_INFO = "DELETE FROM " + TABLE_MORE_INFO + " WHERE " + MORE_INFO_COL_ID + " = ?";
    private final String DELETE_EXTRA_INFO = "DELETE FROM " + TABLE_EXTRA_INFO + " WHERE " + EXTRA_INFO_COL_ID + " = ?";

    private Connection connection = null;
    private PreparedStatement create_tables = null;
    private PreparedStatement queryContacts = null;
    private PreparedStatement getMoreInfo = null;
    private PreparedStatement getExtraInfo = null;
    private PreparedStatement search_contact = null;
    private PreparedStatement search_by_id = null;
    private PreparedStatement addContact = null;
    private PreparedStatement addMoreInfo = null;
    private PreparedStatement addExtraInfo = null;
    private PreparedStatement updateContacts = null;
    private PreparedStatement updateMoreInfo = null;
    private PreparedStatement updateExtraInfo = null;
    private PreparedStatement setFavouriteContact = null;
    private PreparedStatement deleteContact = null;
    private PreparedStatement deleteMoreInfo = null;
    private PreparedStatement deleteExtraInfo = null;

    private Path profilePath;

    private DataSource() {}

    public static DataSource getInstance() {
        return instance;
    }

    public void open() {
        try {
            Path dbPath = Paths.get(DATA_DIRECTORY + File.separator + "database");
            profilePath = Paths.get(DATA_DIRECTORY + File.separator + "images");
            if (!Files.exists(dbPath)) {
                Files.createDirectories(dbPath);
            }
            if (!Files.exists(profilePath)) {
                Files.createDirectories(profilePath);
            }

            connection = DriverManager.getConnection(CONNECTION_URL);
            create_tables = connection.prepareStatement(CREATE_TABLE_CONTACTS);
            create_tables.execute();
            create_tables = connection.prepareStatement(CREATE_TABLE_MORE_INFO);
            create_tables.execute();
            create_tables = connection.prepareStatement(CREATE_TABLE_EXTRA_INFO);
            create_tables.execute();

            queryContacts = connection.prepareStatement(GET_CONTACTS);
            getMoreInfo = connection.prepareStatement(GET_MORE_INFO);
            getExtraInfo = connection.prepareStatement(GET_EXTRA_INFO);
            search_contact = connection.prepareStatement(SEARCH_CONTACT);
            search_by_id = connection.prepareStatement("SELECT * FROM " + TABLE_CONTACTS + " WHERE " + CONTACTS_COL_ID + " = ?");
            addContact = connection.prepareStatement(ADD_CONTACT);
            addMoreInfo = connection.prepareStatement(ADD_MORE_INFO);
            addExtraInfo = connection.prepareStatement(ADD_EXTRA_INFO);
            updateContacts = connection.prepareStatement(UPDATE_CONTACTS);
            updateMoreInfo = connection.prepareStatement(UPDATE_MORE_FIELDS);
            updateExtraInfo = connection.prepareStatement(UPDATE_EXTRA_FIELDS);
            setFavouriteContact = connection.prepareStatement("UPDATE " + TABLE_CONTACTS + " SET " + COL_IS_FAVOURITE + " = ? WHERE " + CONTACTS_COL_ID + " = ?");
            deleteContact = connection.prepareStatement(DELETE_CONTACT);
            deleteMoreInfo = connection.prepareStatement(DELETE_MORE_INFO);
            deleteExtraInfo = connection.prepareStatement(DELETE_EXTRA_INFO);

            System.out.println("[INFO] Connected to database.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to connect to database! " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to create directories! " + e.getMessage());
        }
    }

    public void close() {
        try {
            List<PreparedStatement> pStmtList = new ArrayList<>();
            pStmtList.add(create_tables);
            pStmtList.add(queryContacts);
            pStmtList.add(getMoreInfo);
            pStmtList.add(getExtraInfo);
            pStmtList.add(search_contact);
            pStmtList.add(search_by_id);
            pStmtList.add(addContact);
            pStmtList.add(addMoreInfo);
            pStmtList.add(addExtraInfo);
            pStmtList.add(updateContacts);
            pStmtList.add(updateMoreInfo);
            pStmtList.add(updateExtraInfo);
            pStmtList.add(setFavouriteContact);
            pStmtList.add(deleteContact);
            pStmtList.add(deleteMoreInfo);
            pStmtList.add(deleteExtraInfo);

            for (PreparedStatement stmt : pStmtList) {
                stmt.close();
            }

            if (connection != null) {
                connection.close();
            }
            System.out.println("[INFO] Disconnected from database!");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to close connection from database! " + e.getMessage());
        }
    }

    public ObservableList<Contact> getContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            ResultSet results = queryContacts.executeQuery();
            while (results.next()) {
                Contact contact = new Contact();
                contact.setId(results.getInt(1));
                InputStream profile = results.getBinaryStream(COL_PROFILE);
                if (profile != null) {
                    if (!Files.exists(FileSystems.getDefault().getPath(profilePath.toString() + File.separator + results.getInt(1) + ".jpg"))) {
                        contact.setProfile(ImageReadWrite.readImageFile(profile, String.valueOf(results.getInt(1))));
                    } else {
                        contact.setProfile((new File(profilePath.toString() + File.separator + results.getInt(1) + ".jpg")).toURI().toString());
                    }
                }
                contact.setFirstName(results.getString(COL_FIRST_NAME));
                contact.setLastName(results.getString(COL_LAST_NAME));
                contact.setPhoneNumber(results.getString(COL_PHONE_NUMBER));
                contact.setPhoneType(results.getString(COL_PHONE_TYPE));
                contact.setFavourite(results.getString(COL_IS_FAVOURITE).equals("T"));

                getMoreInfo.setInt(1, results.getInt(1));
                ResultSet moreInfoResult = getMoreInfo.executeQuery();
                if (moreInfoResult.next()) {
                    contact.setEmail(moreInfoResult.getString(COL_EMAIL));
                    contact.setEmailType(moreInfoResult.getString(COL_EMAIL_TYPE));
                    contact.setAddress(moreInfoResult.getString(COL_ADDRESS));
                    contact.setCompany(moreInfoResult.getString(COL_COMPANY));
                    contact.setDate(moreInfoResult.getString(COL_DATE));
                    contact.setDateType(moreInfoResult.getString(COL_DATE_TYPE));
                }
                moreInfoResult.close();

                getExtraInfo.setInt(1, results.getInt(1));
                ResultSet extraInfoResult = getExtraInfo.executeQuery();
                if (extraInfoResult.next()) {
                    contact.setWebsite(extraInfoResult.getString(COL_WEBSITE));
                    contact.setRelation(extraInfoResult.getString(COL_RELATION));
                    contact.setRelationType(extraInfoResult.getString(COL_RELATION_TYPE));
                    contact.setSip(extraInfoResult.getString(COL_SIP));
                    contact.setNotes(extraInfoResult.getString(COL_NOTES));
                }
                extraInfoResult.close();

                contacts.add(contact);
            }
            results.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to retrieve all contacts " + e.getMessage());
        }
        return contacts;
    }

    private void duplicateContactAlert(Contact contact) {
        Alert saveContactAlert = new Alert(Alert.AlertType.INFORMATION);
        saveContactAlert.setTitle("Contact");
        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();
        if (lastName == null) {
            lastName = "";
        }
        saveContactAlert.setContentText("Contact " + firstName + " " + lastName + " already exist!");
        saveContactAlert.showAndWait();
    }

    public boolean addContact(Contact contact) {
        boolean status;
        try {
            connection.setAutoCommit(false);
            System.out.println("[INFO] Setting auto-commit to false.");

            search_contact.setString(1, contact.getFirstName());
            search_contact.setString(2, contact.getLastName());
            ResultSet result = search_contact.executeQuery();
            if (result.next()) {
                duplicateContactAlert(contact);
                status = false;
            } else {
                addContact.setString(1, contact.getFirstName());
                addContact.setString(2, contact.getLastName());
                addContact.setString(3, contact.getPhoneNumber());
                addContact.setString(4, contact.getPhoneType());
                String profile = contact.getProfile();
                if (profile != null) {
                    if (!profile.isEmpty()) {
                        addContact.setBytes(5, ImageReadWrite.writeImageFile(profile));
                    }
                }
                addContact.setString(6, contact.isFavourite() ? "T" : "F");

                int affectedRows = addContact.executeUpdate();
                ResultSet key = addContact.getGeneratedKeys();
                if (affectedRows == 1 && key.next()) {

                    if (!contact.getEmail().isEmpty() || !contact.getAddress().isEmpty() ||
                        !contact.getCompany().isEmpty() || !contact.getDate().isEmpty()) {

                        addMoreInfo.setInt(1, key.getInt(1));
                        addMoreInfo.setString(2, contact.getEmail());
                        addMoreInfo.setString(3, contact.getEmailType());
                        addMoreInfo.setString(4, contact.getAddress());
                        addMoreInfo.setString(5, contact.getCompany());
                        addMoreInfo.setString(6, contact.getDate());
                        addMoreInfo.setString(7, contact.getDateType());

                        affectedRows = addMoreInfo.executeUpdate();
                        if (affectedRows != 1) {
                            throw new SQLException("Failed to add more contact info!");
                        }
                    }

                    if (!contact.getWebsite().isEmpty() || !contact.getRelation().isEmpty() ||
                        !contact.getSip().isEmpty() || !contact.getNotes().isEmpty()) {

                        addExtraInfo.setInt(1, key.getInt(1));
                        addExtraInfo.setString(2, contact.getWebsite());
                        addExtraInfo.setString(3, contact.getRelation());
                        addExtraInfo.setString(4, contact.getRelationType());
                        addExtraInfo.setString(5, contact.getSip());
                        addExtraInfo.setString(6, contact.getNotes());

                        affectedRows = addExtraInfo.executeUpdate();
                        if (affectedRows != 1) {
                            throw  new SQLException("Failed to add extra contact info!");
                        }
                    }

                    connection.commit();
                    contact.setId(key.getInt(1));
                    System.out.println("[INFO] Contact added successfully.");
                    status = true;
                } else {
                    throw new SQLException("Failed to add contact!");
                }
                key.close();
            }
            result.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add contact! " + e.getMessage());
            try {
                System.out.println("[INFO] Performing rollback...");
                connection.rollback();
            } catch (SQLException e2) {
                System.err.println("[ERROR] Failed to rollback the changes! " + e.getMessage());
            }
            status = false;
        } finally {
            try {
                System.out.println("[INFO] Resetting default commit behaviour.");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[INFO] Couldn't reset auto-commit! " + e.getMessage());
            }
        }
        return status;
    }

    public boolean updateContact(Contact contact) {
        boolean status;
        try {
            connection.setAutoCommit(false);
            System.out.println("[INFO] Setting auto-commit to false.");

            search_by_id.setInt(1, contact.getId());
            ResultSet result = search_by_id.executeQuery();
            if (result.next()) {
                search_contact.setString(1, contact.getFirstName());
                search_contact.setString(2, contact.getLastName());

                ResultSet searchResult = search_contact.executeQuery();
                if (searchResult.next() && searchResult.getRow() > 1) {
                    duplicateContactAlert(contact);
                    status = false;
                } else {
                    updateContacts.setString(1, contact.getFirstName());
                    updateContacts.setString(2, contact.getLastName());
                    updateContacts.setString(3, contact.getPhoneNumber());
                    updateContacts.setString(4, contact.getPhoneType());
                    String profile = contact.getProfile();
                    if (profile != null) {
                        if (!profile.isEmpty()) {
                            updateContacts.setBytes(5, ImageReadWrite.writeImageFile(profile));
                        }
                    } else {
                        deleteProfile(String.valueOf(result.getInt(1)));
                    }
                    updateContacts.setString(6, contact.isFavourite() ? "T" : "F");
                    updateContacts.setInt(7, result.getInt(1));

                    int affectedRows = updateContacts.executeUpdate();
                    if (affectedRows == 1) {
                        getMoreInfo.setInt(1, result.getInt(1));
                        if (getMoreInfo.executeQuery().next()) {
                            updateMoreInfo.setString(1, contact.getEmail());
                            updateMoreInfo.setString(2, contact.getEmailType());
                            updateMoreInfo.setString(3, contact.getAddress());
                            updateMoreInfo.setString(4, contact.getCompany());
                            updateMoreInfo.setString(5, contact.getDate());
                            updateMoreInfo.setString(6, contact.getDateType());
                            updateMoreInfo.setInt(7, result.getInt(1));

                            affectedRows = updateMoreInfo.executeUpdate();
                            if (affectedRows != 1) {
                                throw new SQLException("Failed to update 'more info'!");
                            }
                        } else {
                            if (!contact.getEmail().isEmpty() || !contact.getAddress().isEmpty() ||
                                    !contact.getCompany().isEmpty() || !contact.getDate().isEmpty()) {

                                addMoreInfo.setInt(1, result.getInt(1));
                                addMoreInfo.setString(2, contact.getEmail());
                                addMoreInfo.setString(3, contact.getEmailType());
                                addMoreInfo.setString(4, contact.getAddress());
                                addMoreInfo.setString(5, contact.getCompany());
                                addMoreInfo.setString(6, contact.getDate());
                                addMoreInfo.setString(7, contact.getDateType());

                                affectedRows = addMoreInfo.executeUpdate();
                                if (affectedRows != 1) {
                                    throw new SQLException("Failed to update 'more info'!");
                                }
                            }
                        }

                        getExtraInfo.setInt(1, result.getInt(1));
                        if (getExtraInfo.executeQuery().next()) {
                            updateExtraInfo.setString(1, contact.getWebsite());
                            updateExtraInfo.setString(2, contact.getRelation());
                            updateExtraInfo.setString(3, contact.getRelationType());
                            updateExtraInfo.setString(4, contact.getSip());
                            updateExtraInfo.setString(5, contact.getNotes());
                            updateExtraInfo.setInt(6, result.getInt(1));

                            affectedRows = updateExtraInfo.executeUpdate();
                            if (affectedRows != 1) {
                                throw new SQLException("Failed to update 'extra info'!");
                            }
                        } else {
                            if (!contact.getWebsite().isEmpty() || !contact.getRelation().isEmpty() ||
                                    !contact.getSip().isEmpty() || !contact.getNotes().isEmpty()) {

                                addExtraInfo.setInt(1, result.getInt(1));
                                addExtraInfo.setString(2, contact.getWebsite());
                                addExtraInfo.setString(3, contact.getRelation());
                                addExtraInfo.setString(4, contact.getRelationType());
                                addExtraInfo.setString(5, contact.getSip());
                                addExtraInfo.setString(6, contact.getNotes());

                                affectedRows = addExtraInfo.executeUpdate();
                                if (affectedRows != 1) {
                                    throw new SQLException("Failed to update 'extra info'!");
                                }
                            }
                        }
                        connection.commit();
                        status = true;
                        System.out.println("[INFO] Contact updated successfully.");
                    } else {
                        throw new SQLException("Failed to update Contact!");
                    }
                }
            } else {
                throw new SQLException("Contact doesn't exist!");
            }
            result.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update contact! " + e.getMessage());
            try {
                System.out.println("[INFO] Performing rollback...");
                connection.rollback();
            } catch (SQLException e2) {
                System.err.println("[ERROR] Failed to rollback the changes! " + e.getMessage());
            }
            status = false;
        } finally {
            try {
                System.out.println("[INFO] Resetting default commit behaviour.");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[INFO] Couldn't reset auto-commit! " + e.getMessage());
            }
        }
        return status;
    }

    public boolean setFavourite(Contact contact) {
        boolean status;
        try {
            connection.setAutoCommit(false);
            System.out.println("[INFO] Setting auto-commit to false.");

            search_by_id.setInt(1, contact.getId());
            ResultSet result = search_by_id.executeQuery();
            if (result.next()) {
                setFavouriteContact.setString(1, contact.isFavourite() ? "T" : "F");
                setFavouriteContact.setInt(2, contact.getId());

                int affectedRow = setFavouriteContact.executeUpdate();
                if (affectedRow != 1) {
                    throw new SQLException();
                } else {
                    connection.commit();
                    status = true;
                    System.out.println("[INFO] Contact updated successfully.");
                }
            } else {
                throw new SQLException("Contact doesn't exist!");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to set favourite contact! " + e.getMessage());
            try {
                System.out.println("[INFO] Performing rollback...");
                connection.rollback();
            } catch (SQLException e2) {
                System.err.println("[ERROR] Failed to rollback the changes! " + e.getMessage());
            }
            status = false;
        } finally {
            try {
                System.out.println("[INFO] Resetting default commit behaviour.");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[INFO] Couldn't reset auto-commit! " + e.getMessage());
            }
        }
        return status;
    }

    public boolean deleteContact(Contact contact) {
        boolean status;
        try {
            connection.setAutoCommit(false);
            System.out.println("[INFO] Setting auto-commit to false.");

            search_contact.setString(1, contact.getFirstName());
            search_contact.setString(2, contact.getLastName());
            ResultSet result = search_contact.executeQuery();
            if (result.next()) {
                deleteContact.setInt(1, result.getInt(1));
                int affectedRows = deleteContact.executeUpdate();
                if (affectedRows == 1) {

                    if (contact.getEmail() != null || contact.getAddress() != null ||
                        contact.getCompany() != null || contact.getDate() != null) {
                        if (!contact.getEmail().isEmpty() || !contact.getAddress().isEmpty() ||
                                !contact.getCompany().isEmpty() || !contact.getDate().isEmpty()) {

                            deleteMoreInfo.setInt(1, result.getInt(1));
                            affectedRows = deleteMoreInfo.executeUpdate();
                            if (affectedRows != 1) {
                                throw new SQLException("Failed to delete more contact info!");
                            }
                        }
                    }

                    if (contact.getWebsite() != null || contact.getRelation() != null ||
                        contact.getSip() != null || contact.getNotes() != null) {
                        if (!contact.getWebsite().isEmpty() || !contact.getRelation().isEmpty() ||
                                !contact.getSip().isEmpty() || !contact.getNotes().isEmpty()) {

                            deleteExtraInfo.setInt(1, result.getInt(1));
                            affectedRows = deleteExtraInfo.executeUpdate();
                            if (affectedRows != 1) {
                                throw new SQLException("Failed to delete extra contact info!");
                            }
                        }
                    }

                    deleteProfile(String.valueOf(result.getInt(1)));

                    connection.commit();
                    status = true;
                    System.out.println("[INFO] Contact deleted successfully.");
                } else {
                    throw new SQLException("Failed to delete contact!");
                }
            } else {
                System.err.println("Contact doesn't exists");
                status = false;
            }
            result.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to delete contact! " + e.getMessage());
            try {
                System.out.println("[INFO] Performing rollback...");
                connection.rollback();
            } catch (SQLException e2) {
                System.err.println("[ERROR] Failed to rollback the changes! " + e.getMessage());
            }
            status = false;
        } finally {
            try {
                System.out.println("[INFO] Resetting default commit behaviour.");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[INFO] Couldn't reset auto-commit! " + e.getMessage());
            }
        }
        return status;
    }

    private void deleteProfile(String filename) throws IOException {
        if (filename != null) {
            Path filepath = FileSystems.getDefault().getPath(profilePath.toString() + File.separator + filename + ".jpg");
            Files.deleteIfExists(filepath);
        }
    }
}
