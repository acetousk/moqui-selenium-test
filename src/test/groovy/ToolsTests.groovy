import Selenium.Helper
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.Keys
import org.openqa.selenium.Point
import spock.lang.Shared
import spock.lang.Specification

class ToolsTests extends Specification{

  @Shared String apps = "vapps"

  @Shared String entityName =  "ZZEntityName"
  @Shared String packageName = "zzpackagename"
  @Shared String entity = "Entity";
  @Shared String filter = "sqlfilter";
  @Shared String accountId = "BillingAccountID"
  @Shared String partyId = "BillFromPartyID"
  @Shared String accountLimit = "1000"
  @Shared String description = "description"
  @Shared String externalAccountId = "72832"
  @Shared String transId = "accountTransID"
  @Shared String cron = "cronString"

  @Shared Helper helper = new Helper()

  def setupSpec(){
    helper.setupSpec()

    entityName = helper.appendString(entityName)
    packageName = helper.appendString(packageName)
    entity = helper.appendString(entity)
    filter = helper.appendString(filter)
    accountId = helper.appendString(accountId)
    partyId = helper.appendString(partyId)
    description = helper.appendString(description)
    transId = helper.appendString(transId)
    cron = helper.appendString(cron)
  }
  def cleanupSpec(){ helper.cleanupSpec() }
  def setup(){ helper.setup(apps,"Tools") }
  def cleanup(){ helper.cleanup(apps) }

  def "Entity/DataImport test"(){
    when:
    helper.clickByText("Data Import")

    helper.clearAndSendKeysBySelector("input[name='timeout'","30")
    helper.clickBySelector("#ImportData_dummyFks")
//    helper.sendKeysBySelector("input[name='types']","a_data_type")
//    helper.sendKeysBySelector("input[name='components']","a_data_component")

    helper.clickBySelector("a[aria-controls='ImportData_accordion_collapse2']")
    helper.sendKeysBySelector("input[name='location']","blah")
    helper.clickBySelector("a[aria-controls='ImportData_accordion_collapse3']")
    helper.clearAndSendKeysBySelector("textarea[name='xmlText']","<xml>a text</xml>")
    helper.clickBySelector("a[aria-controls='ImportData_accordion_collapse4']")
    helper.clearAndSendKeysBySelector("textarea[name='jsonText']","json=\"a text\"")
    helper.clickBySelector("a[aria-controls='ImportData_accordion_collapse5']")
    helper.clearAndSendKeysBySelector("textarea[name='csvText']",";a;c,s,v;t,e,x,t;")

    helper.pause(1)

    helper.clickBySelector("button[name='checkOnly']")
    String message1 = helper.getNotificationMessage(apps)

    helper.clickBySelector("button[name='onlyCreate']")
    String alert1 = helper.getAlertTextAndAccept()
    String message2 = helper.getNotificationMessage(apps)

    helper.clickBySelector("button[name='importButton']")
    String alert2 = helper.getAlertTextAndAccept()
    String message3 = helper.getNotificationMessage(apps)

    String url = helper.driver.getCurrentUrl()

    then:
    /**
     * the following messages aren't the same between vapps and qapps because qapps is in beta still
     */
//    message1 == "Submit successful"
//    message2 == "Submit successful"
//    message3 == "Submit successful"

    alert1 == "Are you sure you want to load data, only creating missing records?"
    alert2 == "Are you sure you want to load data, creating new and updating existing records? If in doubt, cancel this and double check."
    url.contains("Entity/DataImport")
  }

  def "Entity/DataExport test"(){
    when:
    helper.clickByText("Data Export")
    helper.clearAndSendKeysBySelector("input[name='dependentLevels']","5")
    helper.sendKeysBySelector("input[name='masterName']","MasterName")
    helper.sendKeysBySelector("input[name='orderBy']","OrderBy")
    helper.clearAndSendKeysBySelector("input[name='fromDate']","2020-02-02 13:45")
    helper.clearAndSendKeysBySelector("input[name='thruDate']","2040-06-02 13:20")

    /**
    helper.clickBySelector("#ExportData_fileType_1 > input:nth-child(1)")
    helper.clickBySelector("#ExportData_output > input:nth-child(1)")
    //*/

    helper.sendKeysBySelector("input[name='path']","/example/path")
    helper.clickBySelector("button[name='submitButton']")
//    String text1 = helper.driver.findElement(By.cssSelector(".text-inline")).getText()
//    helper.select(By.cssSelector(".select2-search__field"),10)
    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("Entity/DataExport")
//    text1 == "No entity names specified, not exporting anything."
  }


  def "Entity/DataSnapshot test"(){
    when:
    helper.clickElement(By.linkText("Data Snapshots"))
    helper.clickBySelector("button[onclick=\"return confirm('Really drop all known foreign keys?')\"]")
    String alert1 = helper.getAlertTextAndAccept()

    helper.clickBySelector("button[onclick=\"return confirm('Really create all missing foreign keys?')\"]")
    String alert2 = helper.getAlertTextAndAccept()

    helper.clickByPath("//*[normalize-space() = 'Export Snapshot']")
//    helper.select(By.cssSelector("input[type='search']"),5)
//    helper.clickBySelector("input[type='search']")
    helper.clickById("ExportData_filePerEntity")
    helper.clickById("ExportData_submitButton")

    //TODO: figure out why the following isn't waiting to click
//        clickById("ImportDialog_0-button")
//        clickBySelector("#ImportForm_0_dummyFks_0 > input:nth-child(1)")
//        clickBySelector("#ImportForm_0_useTryInsert_0 > input:nth-child(1)")
//        clearAndSendKeys(By.id("ImportForm_0_transactionTimeout_0"),"10000")
//        clickBySelector("ImportForm_0_submitButton_0")
//        clickBySelector("div.row:nth-child(8) > div:nth-child(5) > form:nth-child(1) > button:nth-child(2)")
//        helper.driver.navigate().refresh()
//        helper.clickByPath("//*[normalize-space() = 'Import']")
//        helper.clickById("ImportForm_0_dummyFks_0")
//        helper.clickById("ImportForm_0_useTryInsert_0")
//        helper.clickById("ImportForm_0_disableEntityEca_0_1")
//        helper.clickById("ImportForm_0_disableAuditLog_0_1")
//        helper.clickById("ImportForm_0_disableFkCreate_0_1")
//        helper.clickById("ImportForm_0_disableDataFeed_0_1")
//        helper.clearAndSendKeysBySelector("input[name='transactionTimeout']","1000")
//        helper.clickById("ImportForm_0_submitButton_0")


    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("Entity/DataSnapshot")
    alert1 == "Really drop all known foreign keys?"
    alert2 == "Really create all missing foreign keys?"
  }

  def "Entity/Entities test"(){
    when:
    helper.clickElement(By.linkText("Entities"))
    helper.pause(1)

    if(apps=="qapps"){
      helper.sendKeysBySelector("input[name='filterRegexp']","BitcoinWallet")
    } else if(apps=="vapps"){
      helper.sendKeysBySelector("#FilterForm_filterRegexp","BitcoinWallet")
    }

    helper.clickById("FilterForm_submitButton")
    helper.clickById("EntityList_detail_0_detail")
    helper.pause(1)

    helper.clickById("RelatedEntities_link_0_find")
    helper.clickBySelector("#ListEntityValue_edit_0")
    String message1 = helper.getNotificationMessage(apps)
    helper.clearAndSendKeysBySelector("input[name='description'","Vxrbt ************914")
    helper.sendKeysBySelector("input[name='ledgerBalance'","72832")
    helper.sendKeysBySelector("input[name='availableBalance'","2131")
    helper.clearAndSendKeysBySelector("input[name='balanceDate'","2020-04-25 09:00")
    helper.sendKeysBySelector("input[name='imageUrl'","https://youtu.be/5SoJIEwe3cA?t=1696")
    helper.sendKeysBySelector("input[name='paymentFraudEvidenceId'","rip 2020")
    //TODO: didn't finish this test
    //  helper.clickBySelector("button[name='submitButton']")
    //  helper.clickByPath("helper.clickByPath(\"//*[normalize-space() = 'Update']\")")
    //  String message2 = helper.getNotificationMessage(apps)
    //  helper.clickById("RelatedEntities_link_0_edit")
    //  helper.clearAndSendKeysBySelector("input[name='enumCode']","5")
    //  helper.clickById("button[name='submitButton']")
    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("Entity")
    message1 == "Submit successful"
  }

  def "Entity/DataViews test"(){
    when:
    helper.clickElement(By.linkText("Data Views"))
    helper.sendKeysBySelector("input[name='dbViewEntityName']",entityName)
    helper.sendKeysBySelector("input[name='packageName']",packageName)
    helper.clickBySelector("button[name='submitButton']")
    String message1 = helper.getNotificationMessage(apps);

    helper.clickBySelector("#ListDbViewEntities_edit_0_edit")
    helper.clickBySelector("button[name='submitButton']")
    String message2 = helper.getNotificationMessage(apps);

    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("DataView")
    message1 == "Submit successful"
    message2 == "Submit successful"
  }

  //TODO:start here
  def "Entity/SpeedTest test"(){
    when:
    helper.clickByText("Speed Test")
    helper.clearAndSendKeysBySelector("input[name='baseCalls']","100")
    helper.clickById("SelectBaseCalls_submitButton")
    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("Entity/SpeedTest")
  }

  def "Entity/QueryStats test"(){
    when:
    helper.clickByText("Query Stats")
    helper.clickBySelector("button[name='submitButton']")

    helper.sendKeysBySelector("input[name='entityFilter']",entity)
    helper.sendKeysBySelector("input[name='sqlFilter']",filter)

    helper.clickByPath("//*[normalize-space() = 'Filter']")
    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("Entity/QueryStats")
  }

  def "Generaltools/AutoScreens test"(){
    when:
    helper.clickByText("Auto Screens")
//    helper.clickBySelector("#select2-SelectEntity_aen-container")
//    helper.select(By.cssSelector("div[id='SelectEntity_aen']"),10)
//    helper.select(By.cssSelector("div[id='SelectEntity_aen']"),7)
    helper.clickByText("Billing Account")
    helper.clickByPath("//*[normalize-space() = 'New Value']")
    if(apps == "qapps"){
      helper.sendKeysBySelector("input[name='billingAccountId']",accountId)
      helper.sendKeysBySelector("input[name='billFromPartyId']",partyId)
      helper.sendKeysBySelector("input[name='accountLimit']",accountLimit)
      helper.sendKeysBySelector("input[name='description']",description)
      helper.sendKeysBySelector("input[name='externalAccountId']",externalAccountId)
      helper.clickBySelector("button[name='submitButton']")
    } else if(apps == "vapps"){
      helper.sendKeysBySelector("#CreateEntityValue_billingAccountId",accountId)
      helper.sendKeysBySelector("#CreateEntityValue_billFromPartyId",partyId)
      helper.sendKeysBySelector("#CreateEntityValue_accountLimit",accountLimit)
      helper.sendKeysBySelector("#CreateEntityValue_description",description)
      helper.sendKeysBySelector("#CreateEntityValue_externalAccountId",externalAccountId)
      helper.clickBySelector("#CreateEntityValue_submitButton")
    }
    String message1 = helper.getNotificationMessage(apps)

    //TODO: Maybe figure out what this did and use it?
    //  helper.clickBySelector("#FindEntityValue > fieldset:nth-child(2) > div:nth-child(1) > div:nth-child(2) > span:nth-child(1) > span:nth-child(1) > input:nth-child(1)")
    //  helper.sendKeys(By.id("FindEntityValue_opportunityStageId"),"Stage Id")
    //  helper.sendKeys(By.id("FindEntityValue_description"),"description")
    //  helper.sendKeys(By.id("FindEntityValue_defaultProbability_from"),"0")
    //  helper.sendKeys(By.id("FindEntityValue_defaultProbability_thru"),"5")
    //  helper.sendKeys(By.id("FindEntityValue_sequenceNum_from"),"1")
    //  helper.sendKeys(By.id("FindEntityValue_sequenceNum_thru"),"3")
    //  helper.clickById("FindEntityValue_submitButton")

    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("AutoScreen/AutoFind")
    message1 == "Submit successful"

  }

//TODO: Artifact Stats does not work
//  def "Generaltools/ArtifactStats test"(){
//    when:
//    helper.clickByText("Artifact Stats")
//    helper.clickById("ServiceInfoList_name_13_serviceReference")//org.moqui
//    //helper.clickById("SelectServiceInput_submitButton")//start moqui.org's service
//    helper.clickBySelector("tr.form-list-nav-row:nth-child(1) > th:nth-child(1) > nav:nth-child(1) > ul:nth-child(1) > li:nth-child(4) > a:nth-child(1)")//page 2
//    helper.clickById("ServiceList_detail_0_serviceDetail")
//    String url = helper.driver.getCurrentUrl()
//    then:
//    url.contains("Service/ServiceDetail")
//  }

  def "Generaltools/Services test"(){
    when:
    helper.clickByText("Services")
    helper.clickById("ServiceList_run_22_serviceRun")//mantle.account.FinancialAccountServices.reverse#InvoiceTransactions
    helper.sendKeysBySelector("input[name='finAccountTransId']",transId)
    helper.clickByPath("//*[normalize-space() = 'Run Service']")
    helper.sendKeysBySelector("input[name='cronString']",cron)

    String url = helper.driver.getCurrentUrl()

    then:
    url.contains("Service/ServiceRun")
  }
}
