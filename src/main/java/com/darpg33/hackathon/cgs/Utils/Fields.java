package com.darpg33.hackathon.cgs.Utils;

public class Fields {


    private static final String TAG = "Fields";

    /**
     *  Below consists of Database fields
     *  Collections : Requests, Users, Mediators, Departments
     *
     *  SubCollections :
     *  Users -> {User Id} -> MyRequests -> {request id} ->
     *  Requests -> {request id} -> Actions -> {action id} ->
     *  Mediators -> Users ->
     *  Mediators -> Requests -> All Requests -> {request id} ->
     *  Departments -> {department name} -> Users -> {user id} ->
     *  Departments -> {department name} -> Requests -> {request id} ->
     *
     */

    //Collections
    public static final String DBC_REQUESTS = "Requests";
    public static final String DBC_USERS = "Users";
    public static final String DBC_USERS_MY_REQUESTS = "MyRequests";

    public static final String DBC_MEDIATORS = "Mediators";
    public static final String DBC_MED_ALL_REQUESTS = "All Requests";
    public static final String DBC_DEPARTMENTS = "Departments";
    public static final String DBC_DEPT_USERS = "Users";
    public static final String DBC_DEPT_REQUESTS = "Requests";
    public static final String DBC_REQ_ACTIONS = "Actions";


    //Requests fields
    public static final String DB_GR_DESCRIPTION = "grievance_description";
    public static final String DB_GR_REQUEST_ID = "grievance_request_id";
    public static final String DB_GR_STATUS = "grievance_status";
    public static final String DB_GR_TIMESTAMP = "grievance_timestamp";
    public static final String DB_GR_TITLE = "grievance_title";
    public static final String DB_GR_USER_ID = "grievance_user_id";
    public static final String DB_GR_CATEGORY = "grievance_category";
    public static final String DB_GR_ATTACHMENTS = "grievance_attachments";
    public static final String DB_GR_PRIORITY = "grievance_priority";
    public static final String DB_GR_PRIVACY = "grievance_privacy";
    public static final String DB_GR_HANDLED_BY = "grievance_handled_by";
    public static final String DB_GR_REQUESTED_BY = "grievance_requested_by";
    public static final String DB_GR_UPVOTES = "grievance_upvotes";


    //Request attachment fields
    public static final String DB_GR_ATTACHMENT_NAME = "name";
    public static final String DB_GR_ATTACHMENT_PATH = "path";
    public static final String DB_GR_ATTACHMENT_TIMESTAMP = "timestamp";
    public static final String DB_GR_ATTACHMENT_LOCATION = "location";
    public static final String DB_GR_ATTACHMENT_TYPE = "type";


    //Request Action Fields
    public static final String DB_GR_ACTION_DESCRIPTION = "action_description";
    public static final String DB_GR_ACTION_INFO = "action_info";
    public static final String DB_GR_ACTION_PERFORMED = "action_performed";
    public static final String DB_GR_ACTION_TIMESTAMP = "action_timestamp";
    public static final String DB_GR_ACTION_USER_EMAIL = "action_user_email";
    public static final String DB_GR_ACTION_USER_ID = "action_user_id";
    public static final String DB_GR_ACTION_USER_TYPE = "action_user_type";
    public static final String DB_GR_ACTION_USERNAME = "action_username";


    //Users fields
    public static final String DB_USER_FIRSTNAME = "first_name";
    public static final String DB_USER_LASTNAME = "last_name";
    public static final String DB_USER_ADDRESS = "address";
    public static final String DB_USER_COUNTRY = "country";
    public static final String DB_USER_EMAIL_ID = "email_id";
    public static final String DB_USER_GENDER = "gender";
    public static final String DB_USER_DISTRICT = "district";
    public static final String DB_USER_STATE = "state";
    public static final String DB_USER_TIMESTAMP = "timestamp";
    public static final String DB_USER_PHONE_NUMBER = "phone_number";
    public static final String DB_USER_USER_ID = "user_id";
    public static final String DB_USER_USER_TYPE = "user_type";
    public static final String DB_USER_REGISTERED = "registered";
    public static final String DB_USER_PINCODE = "pin_code";
    public static final String DB_USER_DEPARTMENT = "user_department";


    /**
     * Grievance status values - Pending, In Process, Resolved
     */
    public static final String GR_STATUS_PENDING = "Pending";
    public static final String GR_STATUS_IN_PROCESS = "In Process";
    public static final String GR_STATUS_RESOLVED = "Resolved";


    /**
     * Roles - department InCharge,worker, mediator,citizen
     */
    public static final String USER_TYPE_DEP_INCHARGE = "department in-charge";
    public static final String USER_TYPE_DEP_WORKER = "worker";
    public static final String USER_TYPE_MEDIATOR = "mediator";
    public static final String USER_TYPE_CITIZEN = "citizen";


    /**
     * Bundles
     */
    public static final String BUNDLE_USER_INFO = "user_info";


    /**
     * Shared preferences
     */

    public static final String SHARED_PREF_SETTINGS = "settings";
    public static final String SHARED_PREF_SETTINGS_LOCALE = "locale";



}
