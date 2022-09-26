package com.techsophy.tsf.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jdk.jfr.Description;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.techsophy.tsf.api.TokenManager.getToken;
import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class APIGatewayIT {
    static List<String> idList = null;
    static String grpId = null;
    static String ruleId = null;
    static String formId = null;
    static String docId = null;
    static String userId = new String();
    static String themeId = new String();
    static String userIdKey = new String();
    static String tenantId = null;
    static String caseId = "";
    static String escalationId = null;
    static List<String> runTimeFormidList = null;
    static String runTimeFormId = null;
    static String runTimeFormDataId = null;
    static BigInteger checklistId = null;
    static  String checkListItem = null;


    static  BigInteger checkListGroupId=null;
    static List<String> workFlowIdList = null;
    static String workFlowId = null;


    @org.junit.jupiter.api.Test
    void getAllGroups() {
        HashMap<String, String> groupParams = new HashMap<String, String>();
        groupParams.put("page", "");
        groupParams.put("size", "");
        groupParams.put("sort-by", "");
        given().header("Authorization", "Bearer " + getToken()).queryParams(groupParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/keycloak/groups")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    void getGroupById() {

        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/accounts/v1/keycloak/groups/e6541116-9ade-4f57-8972-7f488cae5311");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @Order(1)
    @org.junit.jupiter.api.Test
    void createGroup() {

        String payload = "{\"name\": \"test56\",\n" +
                "                \"groupId\": \"1121\"}";

        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/accounts/v1/keycloak/groups");
        System.out.println(response);
        idList = response.jsonPath().getList("data.id");
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("GroupsController")
    @Order(2)
    @org.junit.jupiter.api.Test
    void deleteGroup() {
        Response response1 = augmntapi.delete("com/techsophy/tsf/api/accounts/v1/keycloak/groups/" + idList.get(0));
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("groupDataControllerTests")
    @Order(3)
    @org.junit.jupiter.api.Test
    void saveGroup() throws JSONException, JsonProcessingException {
        String groupid = idList.get(0);
        Map<String, Object> jsonObj = new HashMap<>();
        jsonObj.put("name", "test");
        jsonObj.put("description", "sample description");
        jsonObj.put("groupId", groupid);
        ObjectMapper objMap = new ObjectMapper();
        String payLoad = objMap.writeValueAsString(jsonObj);
        Response response = augmntapi.post(payLoad, "com/techsophy/tsf/api/accounts/v1/groups");
        System.out.println(response);
        grpId = response.path("data.id");
        System.out.println(grpId);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("groupDataControllerTests")
    @org.junit.jupiter.api.Test
    void getAllGroupsTest() {
        HashMap<String, String> groupDataParams = new HashMap<String, String>();
        groupDataParams.put("q", "");
        groupDataParams.put("page", "");
        groupDataParams.put("pageSize", "");
        groupDataParams.put("sort-by", "");
        groupDataParams.put("deploymentIdList", "");
        given().header("Authorization", "Bearer " + getToken()).queryParams(groupDataParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/groups")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @Description("groupDataControllerTests")
    @org.junit.jupiter.api.Test
    void getGroupByIdGroupDataController() {
        Response response = augmntapi.get("https://api-gateway.techsophy.com/api/accounts/v1/groups/994125945178796032");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("GroupDataControllerController")
    @Order(4)
    @org.junit.jupiter.api.Test
    void deleteGroupDataController() {
        Response response1 = augmntapi.delete("com/techsophy/tsf/api/accounts/v1/groups/" + grpId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("GroupsDataController")
    @org.junit.jupiter.api.Test
    void assignRoles() {
        String payload = "{\n" +
                "    \"roles\":[\"DEVELOPER101\"]\n" +
                "}";
        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/accounts/v1/groups/994125945178796032/roles");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Description("ThemesController")
    @Order(5)
    @org.junit.jupiter.api.Test
    void saveThemesData() {
        String payload = "{\n" +
                "    \n" +
                "    \"name\":\"test1234\"\n" +
                "}";
        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/accounts/v1/themes");
        System.out.println(response);
        themeId = response.path("data.id");
        System.out.println(themeId);
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Description("ThemesController")
    @org.junit.jupiter.api.Test
    void getThemesDataById() {
        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/accounts/v1/themes/981059835294883840");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("ThemesController")
    @org.junit.jupiter.api.Test
    void getAllThemes() {
        HashMap<String, String> themeParams = new HashMap<String, String>();
        themeParams.put("deploymentIdList", "");
        themeParams.put("q", "");
        themeParams.put("page", "");
        themeParams.put("pageSize", "");
        themeParams.put("sort-by", "");
        given().header("Authorization", "Bearer " + getToken()).queryParams(themeParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/themes")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @Description("ThemesController")
    @Order(6)
    @org.junit.jupiter.api.Test
    void deleteThemesDataById() {
        Response response1 = augmntapi.delete("com/techsophy/tsf/api/accounts/v1/themes/" + themeId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("ThemesController")
    @org.junit.jupiter.api.Test
    void downloadTheme() {
        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/accounts/v1/themes/981059835294883840/export");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("UserManagementInKeyCloakController")
    @Order(7)
    @org.junit.jupiter.api.Test
    void createUser() {

        String payload = "{\n" +
                "    \"userData\": \n" +
                "    {\n" +
                "        \"userName\": \"manasa\",\n" +
                "        \"firstName\": \"sheela\",\n" +
                "        \"lastName\": \"ms\",\n" +
                "        \"mobileNumber\": \"9989020039\",\n" +
                "        \"emailId\": \"manasa.s@techsophy.com\",\n" +
                "        \"department\": \"devops\"\n" +
                "    }\n" +
                "        }";

        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/accounts/v1/keycloak/users");
        userId = response.path("data.id");
        System.out.println(response);
        System.out.println("user"+userId);
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Description("UserManagementInKeyCloakController")
    @org.junit.jupiter.api.Test
    void assignRoleToUser() {
        System.out.println(userId);

        String payload = "{\n" +
                " \"userId\": \"34923bbe-895f-45cb-aff3-528e0fbc8f41\",\n" +
                " \"roles\": [\"DEVELOPER101\"]\n" +
                "}";
        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/accounts/v1/keycloak/users/roles");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Description("UserManagementInKeyCloakController")
    @Order(8)
    @org.junit.jupiter.api.Test
    void deleteUser() {
        HashMap<String, String> userDataParams = new HashMap<String, String>();
        userDataParams.put("username", "manasa");
        given().header("Authorization", "Bearer " + getToken()).queryParams(userDataParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/keycloak/users")
                .contentType(ContentType.JSON)
                .when()
                .delete("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @Description("GroupsController")
    @org.junit.jupiter.api.Test
    void getAllRoles() {

        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/accounts/v1/keycloak/roles");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }


    @Description("UserFormDataController")
    @org.junit.jupiter.api.Test
    void getUserByUserId() {
        HashMap<String, String> userDataParams = new HashMap<String, String>();
        userDataParams.put("only-mandatory-fields", "false");

        given().header("Authorization", "Bearer " + getToken()).queryParams(userDataParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/users/910797699334508544")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @Description("UserFormDataController")
    @org.junit.jupiter.api.Test
    void getAllUsers() {
        HashMap<String, String> userParams = new HashMap<String, String>();
        userParams.put("onlyMandatoryFields", "");
        userParams.put("q", "");
        userParams.put("page", "");
        userParams.put("pageSize", "");
        userParams.put("filterColumn", "");
        userParams.put("filterValue", "");
        given().header("Authorization", "Bearer " + getToken()).queryParams(userParams)

                .baseUri("https://api-gateway.techsophy.com/api/accounts/v1/users")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @Description("UserPreferencesController")
    @org.junit.jupiter.api.Test
    void saveUserPreferencesTheme() {

        String payload = "{\n" +
                " \"themeId\":\"910490074507374592\",\n" +
                " \"userId\":\"910797699334508544\"\n" +
                "}";
        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/accounts/v1/users/preferences");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Description("UserPreferencesController")
    @org.junit.jupiter.api.Test
    void deleteUserPreferencesThemeDataByUserId() {
        Response response1 = augmntapi.delete("com/techsophy/tsf/api/accounts/v1/users/preferences");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @Description("Should Be Able To Create realm ")
    @org.junit.jupiter.api.Test
    void createRealm() {
        String payload = "{\n" +
                "    \"realmName\": \"Test10023\",\n" +
                "    \"displayName\": \"Test123\"\n" +
                "}";

        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/admin/v1//keycloak/realm");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("Should Be Able To Create Tenant ")
    @org.junit.jupiter.api.Test
    void createTenant() {
        String payload = "";
        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/admin/v1//keycloak/realm");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("Should Be Able To Create Tenant ")
    @Order(9)
    @org.junit.jupiter.api.Test
    void saveTenant() {
        String payload = "{\n" +
                "    \"tenantData\": {\n" +
                "        \"userName\": \"tenant2124\",\n" +
                "        \"firstName\": \"kim\",\n" +
                "        \"lastName\": \"jong\",\n" +
                "        \"mobileNumber\": \"9494943456\",\n" +
                "        \"emailId\": \"kim@gmail.com\",\n" +
                "        \"organization\": \"techs0ophy171\",\n" +
                "        \"address\": {\n" +
                "            \"street\": \"brooklyn\",\n" +
                "            \"area\": \"New York\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/admin/v1/keycloak/tenant");
        System.out.println(response);
        tenantId = response.path("data.id");
        System.out.println(tenantId);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("Should Be Able get All Tenants ")
    @org.junit.jupiter.api.Test
    void getAllTenants() {
        HashMap<String, String> TenantParams = new HashMap<String, String>();
        TenantParams.put("page", "1");
        TenantParams.put("size", "5");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(TenantParams)

                .baseUri("https://api-gateway.techsophy.com/api/admin/v1/tenants")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    void getTenantById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/admin/v1/tenants/956421427105792000");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Order(10)
    void deleteTenantById() {
        Response response1 = augmntapi.delete("/com/techsophy/tsf/api/admin/v1/tenants/" + tenantId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(11)
    void saveCase() {
        HashMap<String, String> caseParams = new HashMap<String, String>();
        caseParams.put("name", "case2");
        caseParams.put("content", "content2");
        RestAssured.baseURI = "https://api-gateway.techsophy.com/api/case-modeler/v1";
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + getToken()).queryParams(caseParams);
        Response response = httpRequest.post("/cases");
        caseId = response.path("data.id");
        System.out.println(caseId);
    }

    @org.junit.jupiter.api.Test
    void getAllCases() {
        HashMap<String, String> caseParams = new HashMap<String, String>();
        caseParams.put("include-content", "true");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(caseParams)

                .baseUri("https://api-gateway.techsophy.com/api/case-modeler/v1/cases")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    void getCaseById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/case-modeler/v1/cases/" + caseId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Order(13)
    void deleteCaseById() {
        Response response1 = augmntapi.delete("/com/techsophy/tsf/api/case-modeler/v1/cases/" +caseId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    void getAllCheckLists() {
        HashMap<String, String> checkListParams = new HashMap<String, String>();
        checkListParams.put("page", "2");
        checkListParams.put("size", "1");

        given().header("Authorization", "Bearer " + getToken()).queryParams(checkListParams)

                .baseUri("https://api-gateway.techsophy.com/api/checklist-modeler/v1/checklists")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }


    @org.junit.jupiter.api.Test
    @Order(14)
    void SaveOrUpdateChecklist() {

        String payload = "{\n" +
                "                \"groupList\": [],\n" +
                "                \"name\": \"Checklist\",\n" +
                "                \"description\": \"dsfsf\",\n" +
                "                \"policyChecklistInstractions\": \"fsdsfd\",\n" +
                "                \"checklistResponsibility\": \"optionC\",\n" +
                "                \"checklistReviewerResponsibility\": \"optionB\",\n" +
                "                \"reviewRequired\": false,\n" +
                "                \"overrideApprovalRequired\": true,\n" +
                "                \"suspendFlag\": false\n" +
                "}";

        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/checklist-modeler/v1/checklists");
        System.out.println(response);
        checklistId = BigInteger.valueOf(response.path("data.id"));
        System.out.println(checklistId);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Order(15)
    void GetChecklistById() {

        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/checklist-modeler/v1/checklists/"+checklistId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Order(16)
    void DeleteChecklist() {

        Response response1 = augmntapi.delete("com/techsophy/tsf/api/checklist-modeler/v1/checklists/" +checklistId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void GetAllItems() {
        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/checklist-modeler/v1/checklist-items");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(17)
    void SaveOrUpdateItem() {

        String payload = "{\n" +
                "    \"item\":{\n" +
                "   \"name\": \"Item4\",\n" +
                "                \"description\": \"Description\",\n" +
                "                \"location\": \"locationA\",\n" +
                "                \"activeStartDate\": \"2021-11-15T12:00:00+05:30\",\n" +
                "                \"activeEndDate\": \"2021-11-24T12:00:00+05:30\",\n" +
                "                \"itemCategory\": \"optionC\",\n" +
                "                \"inclusionRule\": \"optionB\",\n" +
                "                \"satisfyRule\": \"optionA\",\n" +
                "                \"processingType\": \"optionB\",\n" +
                "                \"dispositionType\": \"optionA\",\n" +
                "                \"manualOverrideAllowed\": true,\n" +
                "                \"suspendFlag\": false\n" +
                "    }\n" +
                "}";

        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/checklist-engine/v1/checklist-item");
        System.out.println(response);
        checkListItem = response.path("data.id");
        System.out.println(checkListItem);
        Assert.assertEquals(response.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(18)
    void GetChecklistItemById() {
        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/checklist-modeler/v1/checklist-items/"+checkListItem);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(19)
    void DeleteChecklistItem() {

        Response response1 = augmntapi.delete("com/techsophy/tsf/api/checklist-modeler/v1/checklist-items/" +checkListItem);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    void GetAllChecklistGroups() {
        HashMap<String, String> checkListParams = new HashMap<String, String>();
        checkListParams.put("page", "2");
        checkListParams.put("size", "1");

        given().header("Authorization", "Bearer " + getToken()).queryParams(checkListParams)

                .baseUri("https://api-gateway.techsophy.com/api/checklist-modeler/v1/checklist-groups")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }
    @org.junit.jupiter.api.Test
    @Order(20)
    void SaveOrUpdateChecklistGroup() {

        String payload = "{\n" +
                "    \"group\": {\n" +
                "        \"groupName\": \"Group110\",\n" +
                "        \"suspend\": true\n" +
                "    },\n" +
                "    \"itemList\": []\n" +
                "}";

        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/checklist-modeler/v1/checklist-groups");
        System.out.println(response);
        checkListGroupId = BigInteger.valueOf(response.path("data.id"));
        System.out.println(checkListGroupId);
        Assert.assertEquals(response.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(21)
    void GetChecklistGroupById() {
        Response response1 = augmntapi.get("https://api-gateway.techsophy.com/api/checklist-modeler/v1/checklist-groups/"+checkListGroupId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(22)
    void DeleteChecklistGroupById() {

        Response response1 = augmntapi.delete("com/techsophy/tsf/api/checklist-modeler/v1/checklist-groups/" +checkListGroupId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(23)
    void GetAllDocTypes() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/dms/v1/document-types");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);

    }

    @org.junit.jupiter.api.Test
    @Order(24)
    void AddDocType() {
        String payload = "{\n" +
                " \"name\":\"Proof135\",\n" +
                " \"id\":\"887760084437041152\",\n" +
                " \"fileExtensions\":[],\n" +
                " \"maxSize\":1\n" +
                "}";

        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/dms/v1/document-types");
        System.out.println(response);
        docId = response.path("data.id");
        System.out.println(docId);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Order(25)
    void GetDocTypeById() {
        Response response1 = augmntapi.get("com/techsophy/tsf/api/dms/v1/document-types/" +docId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);

    }

    @org.junit.jupiter.api.Test
    @Order(26)
    void DeleteDocTypeById() {
        Response response = augmntapi.delete("com/techsophy/tsf/api/dms/v1/document-types/" +docId);
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void DownloadDoc() {
        HashMap<String, String> Params = new HashMap<String, String>();
        Params.put("version", "1");

        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(Params)

                .baseUri("{{helix-gateway-uri}}/dms/v1/documents/"+docId)
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    void publishDoc() {
        String payload = "{\n" +
                "    \"templateId\": \"61dfcbdd87ca4d31b2f4d674\",\n" +
                "    \"data\": {\n" +
                "        \"username\":\"rupalit\"\n" +
                "    },\n" +
                "    \"documentName\": \"Letter\",\n" +
                "    \"documentPath\": \"12345\",\n" +
                "    \"metaInfo\":{},\n" +
                "    \"documentDescription\": null\n" +
                "}";

        Response response = augmntapi.post(payload, "/dms/v1/documents/publish");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);

    }

    @org.junit.jupiter.api.Test
    void deleteDoc() {
        HashMap<String, String> Params = new HashMap<String, String>();
        Params.put("id", "903142045250703360");

        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(Params)

                .baseUri("http://localhost:8080/dms/v1/documents")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    void getAllDocuments() {
        HashMap<String, String> Params = new HashMap<String, String>();
        Params.put("page", "");
        Params.put("size", "");
        Params.put("q", "");

        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(Params)

                .baseUri("{{domain}}/dms1/v1/documents")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    void getDocTypeById() {
        Response response1 = augmntapi.get("/dms/v1/history/documents/908590613825437696");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);

    }
    @org.junit.jupiter.api.Test
    @Order(27)
    void createEscalationMatrix() {
        String payload = "{\n" +
                "    \"escalationMatrix\": {\n" +
                "        \"name\": \"nan\",\n" +
                "        \"description\": \"nan\",\n" +
                "        \"levels\": [\n" +
                "            {\n" +
                "                \"assigneeType\": \"nandini1\",\n" +
                "                \"name\": \"nandini1\",\n" +
                "                \"sla\": \"10\",\n" +
                "                \"assignee\": \"nandini1\",\n" +
                "                \"levelId\": \"919823802848763904\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"assigneeType\": \"nandini2\",\n" +
                "                \"name\": \"nandini2\",\n" +
                "                \"sla\": \"11\",\n" +
                "                \"assignee\": \"nandini2\",\n" +
                "                \"levelId\": \"919823802848763905\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"assigneeType\": \"nandini4\",\n" +
                "                \"name\": \"nandini4\",\n" +
                "                \"sla\": \"13\",\n" +
                "                \"assignee\": \"nandini4\",\n" +
                "                \"levelId\": \"919823802848763906\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"assigneeType\": \"nandini5\",\n" +
                "                \"name\": \"nandini4\",\n" +
                "                \"sla\": \"13\",\n" +
                "                \"assignee\": \"nandini4\",\n" +
                "                \"levelId\": \"919823802848763907\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"assigneeType\": \"nandini6\",\n" +
                "                \"name\": \"nandini\",\n" +
                "                \"sla\": \"13\",\n" +
                "                \"assignee\": \"nandini4\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/escalation-matrix-modeler/v1/escalation-matrices");
        System.out.println(response);
        escalationId = response.path("data.id");
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void FetchNext() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/escalation-matrix-modeler/v1/escalation-matrix/950413133565612037/levels/950413133565612037");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void getAllData() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/escalation-matrix-modeler/v1/escalation-matrices");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void getEscalationById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/escalation-matrix-modeler/v1/escalation-matrix/950413133565612037");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);

    }

    @org.junit.jupiter.api.Test
    @Order(28)
    void DeleteById() {
        Response response1 = augmntapi.delete("/com/techsophy/tsf/api/escalation-matrix-modeler/v1/escalation-matrix/" + escalationId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);

    }
    @org.junit.jupiter.api.Test
    @Order(29)
    void saveForm() {
        String payload = "{\n" +
                "    \"name\": \"form2002\",\n" +
                "    \"components\": {\n" +
                "        \"key\": \"101\"\n" +
                "    },\n" +
                "    \"properties\": {\n" +
                "        \"property1\": \"value101\"\n" +
                "    },\n" +
                "    \"type\": \"form\"\n" +
                "}";

        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/form-modeler/v1/forms");
        System.out.println(response);
        formId = response.path("data.id");
        System.out.println(formId);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Description("Should Be Able To get form by id ")
    @org.junit.jupiter.api.Test
    @Order(30)
    void getFormById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/form-modeler/v1/forms/" +formId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);

    }

    @Description("Should Be Able To delete form by id ")
    @org.junit.jupiter.api.Test
    @Order(31)
    void deleteFromById() {
        Response response1 = augmntapi.delete("/com/techsophy/tsf/api/form-modeler/v1/forms/" +formId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("Should Be Able get To by id ")
    @org.junit.jupiter.api.Test
    void getMapping() {
        HashMap<String, String> formParams = new HashMap<String, String>();
        formParams.put("include-content", "true");
        formParams.put("type", "acb");
        formParams.put("deployment", "fdfgg");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(formParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-modeler/v1/forms")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @Description("Should Be Able search To by id ")
    @org.junit.jupiter.api.Test
    public static void searchForm() {
        HashMap<String, String> formParams = new HashMap<String, String>();
        formParams.put("idOrNameLike", "912294603643625472");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(formParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-modeler/v1/forms/search")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }
    @Description("Should Be Able To execute DMN")
    @org.junit.jupiter.api.Test
    void executeDMN() {
        String payload = "{\n" +
                "    \"id\": \"878113400043892736\",\n" +
                "    \"variables\":\n" +
                "    {\n" +
                "        \"ticketType\":\"installation\"\n" +
                "    }\n" +
                "}";
        Response response = augmntapi.post(payload, "com/techsophy/tsf/api/rules/v1/execute-dmn");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void fetchDRDDetails() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/rules/v1/dmn/831066622685913100");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void listAllDmn() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/rules/v1/dmn");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("Should Be Able To execute DMN")
    @org.junit.jupiter.api.Test
    void updateDRDDetails() {
        String payload = "{\n" +
                "    \"version\": \"1\",\n" +
                "    \"name\":\"Test cypress\",\n" +
                "    \"id\": \"934926506920218624\",\n" +
                "    \"content\": \"<?xml version=\\\\\\\"1.0\\\\\\\" encoding=\\\\\\\"UTF-8\\\\\\\"?>\\\\n<definitions xmlns=\\\\\\\"https://www.omg.org/spec/DMN/20191111/MODEL/\\\\\\\" xmlns:dmndi=\\\\\\\"https://www.omg.org/spec/DMN/20191111/DMNDI/\\\\\\\" xmlns:dc=\\\\\\\"http://www.omg.org/spec/DMN/20180521/DC/\\\\\\\" xmlns:biodi=\\\\\\\"http://bpmn.io/schema/dmn/biodi/2.0\\\\\\\" xmlns:camunda=\\\\\\\"http://camunda.org/schema/1.0/dmn\\\\\\\" id=\\\\\\\"Definitions_1\\\\\\\" name=\\\\\\\"Definitions_1\\\\\\\" namespace=\\\\\\\"http://camunda.org/schema/1.0/dmn\\\\\\\">\\\\n  <decision id=\\\\\\\"Decision_1\\\\\\\" name=\\\\\\\"Decision 1\\\\\\\">\\\\n    <decisionTable id=\\\\\\\"DecisionTable_014m7wx\\\\\\\" hitPolicy=\\\\\\\"FIRST\\\\\\\">\\\\n      <input id=\\\\\\\"Input_1\\\\\\\" label=\\\\\\\"cibil\\\\\\\" biodi:width=\\\\\\\"192\\\\\\\" camunda:inputVariable=\\\\\\\"cibil\\\\\\\">\\\\n        <inputExpression id=\\\\\\\"InputExpression_1\\\\\\\" typeRef=\\\\\\\"integer\\\\\\\">\\\\n          <text></text>\\\\n        </inputExpression>\\\\n      </input>\\\\n      <output id=\\\\\\\"Output_1\\\\\\\" name=\\\\\\\"risk_score\\\\\\\" typeRef=\\\\\\\"integer\\\\\\\" />\\\\n      <rule id=\\\\\\\"DecisionRule_0a0v14p\\\\\\\">\\\\n        <inputEntry id=\\\\\\\"UnaryTests_11fxzsi\\\\\\\">\\\\n          <text>&gt; 7000</text>\\\\n        </inputEntry>\\\\n        <outputEntry id=\\\\\\\"LiteralExpression_1k3sh69\\\\\\\">\\\\n          <text>500</text>\\\\n        </outputEntry>\\\\n      </rule>\\\\n      <rule id=\\\\\\\"DecisionRule_1k4ktvj\\\\\\\">\\\\n        <inputEntry id=\\\\\\\"UnaryTests_0f3fap3\\\\\\\">\\\\n          <text>&lt;= 700</text>\\\\n        </inputEntry>\\\\n        <outputEntry id=\\\\\\\"LiteralExpression_1wtmhnm\\\\\\\">\\\\n          <text>0</text>\\\\n        </outputEntry>\\\\n      </rule>\\\\n    </decisionTable>\\\\n  </decision>\\\\n  <dmndi:DMNDI>\\\\n    <dmndi:DMNDiagram>\\\\n      <dmndi:DMNShape dmnElementRef=\\\\\\\"Decision_1\\\\\\\">\\\\n        <dc:Bounds height=\\\\\\\"80\\\\\\\" width=\\\\\\\"180\\\\\\\" x=\\\\\\\"180\\\\\\\" y=\\\\\\\"130\\\\\\\" />\\\\n      </dmndi:DMNShape>\\\\n    </dmndi:DMNDiagram>\\\\n  </dmndi:DMNDI>\\\\n</definitions>\\\\n\",\n" +
                "    \"deploymentName\":\"Test\"\n" +
                "}";
        Response response = augmntapi.put(payload, "/com/techsophy/tsf/api/rules/v1/dmn");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void deleteDRDDetails() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/rules/v1/dmn/934926506920218624");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(32)
    void saveRule() {
        HashMap<String, String> ruleParams = new HashMap<String, String>();
        ruleParams.put("name", "testh79");
        ruleParams.put("content", "acbfdf");
        RestAssured.baseURI = "https://api-gateway.techsophy.com/api/rule-modeler/v1";
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + getToken()).queryParams(ruleParams);
        Response response = httpRequest.post("/rules");
        ruleId = response.path("data.id");
        System.out.println(ruleId);
    }

    @org.junit.jupiter.api.Test
    void getAllRules() {
        HashMap<String, String> ruleParams = new HashMap<String, String>();
        ruleParams.put("include-content", "true");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(ruleParams)

                .baseUri("https://api-gateway.techsophy.com/api/rule-modeler/v1/rules")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();

    }

    @org.junit.jupiter.api.Test
    @Order(33)
    void getRuleById() {
        Response response1 = augmntapi.get("com/techsophy/tsf/api/rule-modeler/v1/rules/" +ruleId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Order(34)
    void deleteRuleById() {
        Response response1 = augmntapi.delete("com/techsophy/tsf/api/rule-modeler/v1/rules/" +ruleId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);


    }

    @org.junit.jupiter.api.Test
    void searchRule() {
        HashMap<String, String> ruleParams = new HashMap<String, String>();
        ruleParams.put("idOrNameLike", "908668782468927488");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(ruleParams)

                .baseUri("https://api-gateway.techsophy.com/api/rule-modeler/v1/rules/search")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();

    }
    @org.junit.jupiter.api.Test
    @Order(35)
    void saveRunTimeForm() {
        String payload = "{\n" +
                "    \"name\": \"TestParcel\",\n" +
                "    \"id\": \"994122561634369536\",\n" +
                "    \"components\": {\n" +
                "        \"display\": \"form\",\n" +
                "        \"components\": [\n" +
                "            {\n" +
                "                \"type\": \"button\",\n" +
                "                \"label\": \"Submit\",\n" +
                "                \"key\": \"submit\",\n" +
                "                \"size\": \"md\",\n" +
                "                \"block\": false,\n" +
                "                \"action\": \"submit\",\n" +
                "                \"disableOnInvalid\": true,\n" +
                "                \"theme\": \"primary\",\n" +
                "                \"input\": true,\n" +
                "                \"placeholder\": \"\",\n" +
                "                \"prefix\": \"\",\n" +
                "                \"customClass\": \"\",\n" +
                "                \"suffix\": \"\",\n" +
                "                \"multiple\": false,\n" +
                "                \"defaultValue\": null,\n" +
                "                \"protected\": false,\n" +
                "                \"unique\": false,\n" +
                "                \"persistent\": false,\n" +
                "                \"hidden\": false,\n" +
                "                \"clearOnHide\": true,\n" +
                "                \"refreshOn\": \"\",\n" +
                "                \"redrawOn\": \"\",\n" +
                "                \"tableView\": false,\n" +
                "                \"modalEdit\": false,\n" +
                "                \"dataGridLabel\": true,\n" +
                "                \"labelPosition\": \"top\",\n" +
                "                \"description\": \"\",\n" +
                "                \"errorLabel\": \"\",\n" +
                "                \"tooltip\": \"\",\n" +
                "                \"hideLabel\": false,\n" +
                "                \"tabindex\": \"\",\n" +
                "                \"disabled\": false,\n" +
                "                \"autofocus\": false,\n" +
                "                \"dbIndex\": false,\n" +
                "                \"customDefaultValue\": \"\",\n" +
                "                \"calculateValue\": \"\",\n" +
                "                \"calculateServer\": false,\n" +
                "                \"widget\": {\n" +
                "                    \"type\": \"input\"\n" +
                "                },\n" +
                "                \"attributes\": {},\n" +
                "                \"validateOn\": \"change\",\n" +
                "                \"validate\": {\n" +
                "                    \"required\": false,\n" +
                "                    \"custom\": \"\",\n" +
                "                    \"customPrivate\": false,\n" +
                "                    \"strictDateValidation\": false,\n" +
                "                    \"multiple\": false,\n" +
                "                    \"unique\": false\n" +
                "                },\n" +
                "                \"conditional\": {\n" +
                "                    \"show\": null,\n" +
                "                    \"when\": null,\n" +
                "                    \"eq\": \"\"\n" +
                "                },\n" +
                "                \"overlay\": {\n" +
                "                    \"style\": \"\",\n" +
                "                    \"left\": \"\",\n" +
                "                    \"top\": \"\",\n" +
                "                    \"width\": \"\",\n" +
                "                    \"height\": \"\"\n" +
                "                },\n" +
                "                \"allowCalculateOverride\": false,\n" +
                "                \"encrypted\": false,\n" +
                "                \"showCharCount\": false,\n" +
                "                \"showWordCount\": false,\n" +
                "                \"properties\": {},\n" +
                "                \"allowMultipleMasks\": false,\n" +
                "                \"addons\": [],\n" +
                "                \"leftIcon\": \"\",\n" +
                "                \"rightIcon\": \"\",\n" +
                "                \"id\": \"e2thgfb\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"version\": \"3\",\n" +
                "    \"type\": \"form\"\n" +
                "}";
        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/form-runtime/v1/forms");
        System.out.println(response);
        runTimeFormidList = response.jsonPath().getList("data.id");
        System.out.println();
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void getRuntimeFormById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/form-runtime/v1/forms/13");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void getAllRuntimeForms() {
        HashMap<String, String> runTimeFormParams = new HashMap<String, String>();
        runTimeFormParams.put("include-content", "true");
        runTimeFormParams.put("type", "components");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(runTimeFormParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-runtime/v1/forms")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    @Order(36)
    void deleteFormById() {
        Response response1 = augmntapi.delete("/com/techsophy/tsf/api/form-runtime/v1/forms/994122561634369536");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void searchRuntimeFormByIdOrNameLike() {
        HashMap<String, String> runtimeformParams = new HashMap<String, String>();
        runtimeformParams.put("idOrNameLike", "test");
        runtimeformParams.put("type", "form");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(runtimeformParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-runtime/v1/forms/search")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    @Order(37)
    @Description("FormDataController")
    void saveFormData() {
        String payload = "{\n" +
                "    \"formId\": \"940164331383975936\",\n" +
                "    \"formData\": {\n" +
                "        \"textField\": \"rama10144\"\n" +
                "    },\n" +
                "    \"formMetadata\": {\n" +
                "        \"formVersion\": \"1\"\n" +
                "    }\n" +
                "}";
        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/form-runtime/v1/form-data");
        System.out.println(response);
        runTimeFormDataId = response.path("data.id");
        System.out.println(runTimeFormDataId);
        Assert.assertEquals(response.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    @Description("FormDataController")
    void getAllFormDataByFormId() {
        HashMap<String, String> runTimeFormParams = new HashMap<String, String>();
        runTimeFormParams.put("formId", "940164331383975936");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(runTimeFormParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-runtime/v1/form-data")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    @Order(38)
    void getFormDataById() {
        HashMap<String, String> runTimeFormDataParams = new HashMap<String, String>();
        runTimeFormDataParams.put("formId", "940164331383975936");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(runTimeFormDataParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-runtime/v1/form-data")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    @Description("FormDataController")
    void deleteAllFormDataByFormId() {
        HashMap<String, String> runTimeFormParams = new HashMap<String, String>();
        runTimeFormParams.put("formId", "940164331383975936");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(runTimeFormParams)

                .baseUri("https://api-gateway.techsophy.com/api/form-runtime/v1/form-data")
                .contentType(ContentType.JSON)
                .when()
                .delete("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

//    @org.junit.jupiter.api.Test
//    @Order(39)
//    void deleteFormDataById() {
//        Response response1 = augmntapi.delete("/api/form-runtime/v1/form-data/940164331383975936");
//        System.out.println(response1);
//        Assert.assertEquals(response1.statusCode(), 200);
//    }

    @org.junit.jupiter.api.Test
    @Description("FormDataController")
    void validateFormDataByFormId() {
        String payload = "{\n" +
                "    \"formId\": \"940164331383975936\",\n" +
                "    \"formData\": {\n" +
                "        \"textField\": \"rama10144\"\n" +
                "    },\n" +
                "    \"formMetadata\": {\n" +
                "        \"formVersion\": \"1\"\n" +
                "    }\n" +
                "}";
        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/form-runtime/v1/form-data/validate");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    void downloadTemplateById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/template-modeler/v1/templates/61ba222bad3168749c208ef7");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @Description("Should Be Able get all templates ")
    @org.junit.jupiter.api.Test
    void getAllTemplates() {
        HashMap<String, String> TemplateParams = new HashMap<String, String>();
        TemplateParams.put("q", "");
        TemplateParams.put("filterColumn", "");
        TemplateParams.put("pageSize", "5");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(TemplateParams)

                .baseUri("https://api-gateway.techsophy.com/api/template-modeler/v1/templates")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    void getTemplateById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/template-modeler/v1/templates/61ba222bad3168749c208ef7");
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    void createComment() {
        String payload = "";
        Response response = augmntapi.post(payload, "/com/techsophy/tsf/api/camunda/service/v1/comment/create");
        System.out.println(response);
        Assert.assertEquals(response.statusCode(), 200);
    }
    @org.junit.jupiter.api.Test
    @Order(40)
    void postProcess() {
        HashMap<String, String> processParams = new HashMap<String, String>();
        processParams.put("name", "RestAssured");
        processParams.put("content", "hhh");
        RestAssured.baseURI = "https://api-gateway.techsophy.com/api/process-modeler/v1";
        RequestSpecification httpRequest = RestAssured.given().header("Authorization", "Bearer " + getToken()).queryParams(processParams);
        Response response = httpRequest.post("/processes");
        workFlowId = response.path("data.id");
        System.out.println(workFlowId);
    }

    @org.junit.jupiter.api.Test
    @Order(42)
    void deleteProcess(){
        Response response1 = augmntapi.delete("com/techsophy/tsf/api/process-modeler/v1/processes/" + workFlowId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void getALLProcesses() {
        HashMap<String, String> processParams = new HashMap<String, String>();
        processParams.put("include-content", "true");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(processParams)

                .baseUri("https://api-gateway.techsophy.com/api/process-modeler/v1/processes")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }

    @org.junit.jupiter.api.Test
    @Order(41)
    void getWorkFlowById() {
        Response response1 = augmntapi.get("/com/techsophy/tsf/api/process-modeler/v1/processes/" +workFlowId);
        System.out.println(response1);
        Assert.assertEquals(response1.statusCode(), 200);
    }

    @org.junit.jupiter.api.Test
    void searchProcessBYId() {
        HashMap<String, String> processParams = new HashMap<String, String>();
        processParams.put("idOrNameLike", "992311527845642240");
        RestAssured
                .given().header("Authorization", "Bearer " + getToken()).queryParams(processParams)

                .baseUri("https://api-gateway.techsophy.com/api/process-modeler/v1/processes/search")
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then().assertThat().statusCode(200)
                .log()
                .all();
    }


}
