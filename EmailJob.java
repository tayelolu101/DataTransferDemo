package PaymentEmail;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import com.lowagie.text.pdf.PdfName;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailJob implements Job {
    private Connection conn;
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    public EmailJob() {
    }

    private boolean isValidString(String str) {
        boolean isValid = false;
        if (str != null && str.trim().length() > 0) {
            isValid = true;
        }

        return isValid;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
           // String sendTo = "";
            String sendToBene = "";
            String BeneEmailCopy = "";
          //String BeneEmailCopy_2 = "";
            String ZenEmailCopy_ = "";
            loadProps loader = new loadProps();
            this.conn = loader.getInstance();
            Properties prop = ProperLoader.getProps();
            String query = prop.getProperty("query");
            String query1 = prop.getProperty("query1");
            String query3 = prop.getProperty("query3");
            String Query2 = prop.getProperty("query2");
            String Query4 = prop.getProperty("query4");
            String Query5 = prop.getProperty("query5");
            String QueryFailed = prop.getProperty("queryForFailed");
            BeneEmailCopy = prop.getProperty("mail.beneCopy");
          //  BeneEmailCopy_2 = prop.getProperty("mail.beneCopy_2");
            ZenEmailCopy_ = prop.getProperty("mail.beneCopy_3");
           // String appMode = prop.getProperty("app.mode");
           // String mailDevBen = prop.getProperty("mail.dev.ben");
           // String sendToDev = prop.getProperty("mail.dev.sendto");
            System.out.println(query1 + " query1");
            System.out.println(query + " query");
            System.out.println(this.conn + " conn");
            PreparedStatement ps = this.conn.prepareStatement(query);

            long payment_Id = 0;
            for(ResultSet reader = ps.executeQuery(); reader.next(); this.updateTableColumn(payment_Id, Query2)) {

                LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                String ref = reader.getString("Trans_ref");
                rowsData.put("Your Ref", ref);
                rowsData.put("Beneficiary Name", reader.getString("vendor_name"));
                String senderName = reader.getString("account_name");
                rowsData.put("Sender Name", senderName);
                String date = reader.getDate("payment_due_date").toString();
                rowsData.put("Value Date", date);
                rowsData.put("Currency", reader.getString("payment_currency"));
                String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                rowsData.put("Amount", amt);
                String num = reader.getString("vendor_acct_no");
                /*num = num.substring(0, 2) + "******" + num.substring(8);
                rowsData.put("Beneficiary Account", num);*/
                if(this.isValidString(num)) {
                    if(num.length() > 10){
                        num = num.substring(0, 10);
                    }
                    num = num.substring(0, 2) + "******" + num.substring(8);
                    rowsData.put("Beneficiary Account", num);
                }else {
                    rowsData.put("Beneficiary Account", "BULK UPLOAD");
                }
                payment_Id = (long)reader.getDouble("payment_id");
                rowsData.put("Payment ID", String.valueOf(payment_Id));
                rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                String purpose = reader.getString("vendor_category");
                rowsData.put("Purpose", purpose);
                String status = reader.getString("ptystatus");
                rowsData.put("Status", status);
                String CompanyCode = reader.getString("company_code");
                String VendorCode = reader.getString("vendor_code");
                String send_Ben_Email = reader.getString("SEND_BEN_EMAIL");
                String bene_Email2 = "";
                String bene_Email = reader.getString("bene_email");
                String bene_Name = reader.getString("vendor_name");
                System.out.println("sender : " + BeneEmailCopy);
                String AccountNumber = reader.getString("debit_acct_no");
                if(this.isValidString(AccountNumber)) {
                    if(AccountNumber.length() > 10){
                        AccountNumber = AccountNumber.substring(0, 10);
                    }
                    AccountNumber = AccountNumber.substring(0, 2) + "******" + AccountNumber.substring(8);
                    rowsData.put("Account Number", AccountNumber);
                }
               // AccountNumber = AccountNumber.substring(0, 2) + "******" + AccountNumber.substring(8);
                String AccountName = reader.getString("FULLNAME");
                List validEmailsToBene;
                boolean emailSendToBene;
                if (send_Ben_Email.equalsIgnoreCase("Y")) {
                    if (!this.isValidString(bene_Email)) {
                        if(isValidString(VendorCode)) {
                            bene_Email2 = this.getAlternateBeneEmail(VendorCode, CompanyCode, query1);
                        }
                        if (!this.isValidString(bene_Email2) || ("").equalsIgnoreCase(bene_Email2)) {
                            bene_Email2 = "";
                        }

                        sendToBene = bene_Email2;
                    } else {
                        sendToBene = bene_Email;
                    }

                    logger.info("beneficiary : " + sendToBene);
                    validEmailsToBene = GenerateReceipts.validateEmails(sendToBene);
                    if (!sendToBene.equalsIgnoreCase("") && null != sendToBene && validEmailsToBene.size() > 0) {
                        if (status.equalsIgnoreCase("PROCESSED")) {
                            emailSendToBene = GenerateReceipts.sendEmail(payment_Id, num, bene_Name, validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);
                            if (emailSendToBene) {
                                logger.info("Email successfully sent to beneficiary.");
                            } else {
                                logger.info("Email sending failed to beneficiary.");
                            }
                        } else {
                            logger.info("Payment " + payment_Id + " to " + bene_Name + " is a failed transaction.");
                        }
                    } else {
                        logger.info("No Email address available for this beneficiary or Invalid Email");
                    }
                } else {
                    logger.info("No Email subscription for this beneficiary");
                }

                validEmailsToBene = GenerateReceipts.validateEmails(BeneEmailCopy);
                logger.info("Uploader : " + BeneEmailCopy);
                if (!BeneEmailCopy.equalsIgnoreCase("") && null != BeneEmailCopy && validEmailsToBene.size() > 0) {
                    emailSendToBene = GenerateReceipts.sendEmail(payment_Id, AccountNumber, AccountName, validEmailsToBene, rowsData, ref, amt, status, date, purpose, senderName);
                    if (emailSendToBene) {
                        logger.info("Email sending successful to uploader.");
                    } else {
                        logger.info("Email sending failed to uploader.");
                    }
                } else {
                    logger.info("No Email address available for this uploader or Invalid Email");
                }

            }

            try {
                logger.info("Sending payments advice to other beneficiaries .............");
                this.sendToOtherBeneficiaries(query3, Query2, ZenEmailCopy_);
                logger.info("Done sending payments advice to other beneficiaries .............");
            } catch (Exception var38) {
                logger.info("Sending Email failed");
                var38.printStackTrace();
                logger.error("Error : " + var38.getMessage());
            }

            try {
                logger.info("Sending payment advice for failed payments .............");
                this.sendFailedPayments(QueryFailed, Query2);
                logger.info("Done sending payments advice for failed payments .............");
            } catch (Exception var38) {
                logger.info("Sending Email failed");
                 var38.printStackTrace();
            }
        } catch (Exception var39) {
            var39.printStackTrace();

            try {
                if (!this.conn.isClosed()) {
                    this.conn.close();
                }
            } catch (Exception var37) {
                var37.printStackTrace();
            }
        }

    }

    private String getAlternateBeneEmail(String companyId, String Code, String Sql) {
        String NewBeneEmail = "";

        try {
            PreparedStatement ps = this.conn.prepareStatement(Sql);
            ps.setString(1, companyId);
            ps.setString(2, Code);

            for(ResultSet result = ps.executeQuery(); result.next(); NewBeneEmail = result.getString("vendor_email")) {

            }
        } catch (Exception var7) {
            var7.printStackTrace();
            logger.error("Error message : " + var7);

            try {
                if (!this.conn.isClosed()) {
                    this.conn.close();
                }
            } catch (Exception var37) {
                var37.printStackTrace();
            }
        }

        return NewBeneEmail;
    }

    private void updateTableColumn(Long paymentId, String Query2) {

            if (paymentId != null) {
                try {
                PreparedStatement prepare = this.conn.prepareStatement(Query2);
                prepare.setString(1, "Y");
                prepare.setDouble(2, (double)paymentId);
                prepare.execute();
                logger.info("Table column for payment " + paymentId + " has been successful updated");

                 } catch (Exception var4) {
                    var4.printStackTrace();
                      logger.info("Update failed for payment : " + paymentId);
                     try {
                        if (!this.conn.isClosed()) {
                           this.conn.close();
                        }
                     } catch (Exception var37) {
                          var37.printStackTrace();
                     }
                }

            }
    }

    private void updateBulkTableColumn(Long paymentId, String Query4) {
        if (paymentId != null) {
            try {
                PreparedStatement prepare = this.conn.prepareStatement(Query4);
                prepare.setString(1, "Y");
                prepare.setDouble(2, (double) paymentId);
                prepare.execute();
                logger.info("Table column for payment " + paymentId + " has been successful updated");
            } catch (Exception var4) {
                var4.printStackTrace();
                logger.info("Update failed for payment : " + paymentId);
                try {
                    if (!this.conn.isClosed()) {
                        this.conn.close();
                    }
                } catch (Exception var37) {
                    var37.printStackTrace();
                }
            }
        }
    }

    private void sendFailedPayments(String queryfailed, String updateQuery){

        System.out.println("SQL : " + queryfailed);
        System.out.println("sqlQuery : " + updateQuery);
        Long payment__Id = null;

        try {
            PreparedStatement ps = this.conn.prepareStatement(queryfailed);
            ResultSet reader = ps.executeQuery();

            while(reader.next()) {
                try {
                    LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                    String ref = reader.getString("Trans_ref");
                    rowsData.put("Your Ref", ref);
                    String beneName = reader.getString("vendor_name");
                    rowsData.put("Beneficiary Name", beneName);
                    String senderName = reader.getString("account_name");
                    rowsData.put("Sender Name", senderName);
                    String date = reader.getDate("payment_due_date").toString();
                    rowsData.put("Value Date", date);
                    rowsData.put("Currency", reader.getString("payment_currency"));
                    String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                    rowsData.put("Amount", amt);
                    String num = reader.getString("vendor_acct_no");
                    if (this.isValidString(num)) {
                        if(num.length() > 10){
                            num = num.substring(0, 10);
                        }
                        num = num.substring(0, 2) + "******" + num.substring(8);
                        rowsData.put("Beneficiary Account", num);
                    } else {
                        rowsData.put("Beneficiary Account", "BULK UPLOAD");
                    }
                    payment__Id = (long) reader.getDouble("payment_id");
                    rowsData.put("Payment ID", String.valueOf(payment__Id));
                    rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                    String purpose = reader.getString("vendor_category");
                    rowsData.put("Purpose", purpose);
                    String status = reader.getString("ptystatus");
                    rowsData.put("Status", status);
                    String AcctName = reader.getString("fullname");
                    String uploaderEmail = reader.getString("email");
                    if (this.isValidString(uploaderEmail)) {
                        List<String> validEmail = GenerateReceipts.validateEmails(uploaderEmail);
                        if (!uploaderEmail.equalsIgnoreCase("") && null != uploaderEmail && validEmail.size() > 0) {
                           // boolean sent = false;
                            GenerateReceipts.sendEmail(payment__Id, num, AcctName, validEmail, rowsData, ref, amt, status, date, purpose, senderName);
                            //  if (sent) {
                            //   List<String> validEmailToZenith = GenerateReceipts.validateEmails(ZenithEmail);
                            //   GenerateReceipts.sendEmail(payment__Id, num, beneName, validEmailToZenith, rowsData, ref, amt, status, date, purpose, senderName);
                            //  } else {
                            //   logger.info("failed to update column for payment_Id " + payment__Id);
                            //  }
                        }
                    } else {
                        logger.info("Invalid Email address");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    logger.error("An unexpected error for payment with id " + payment__Id);
                }
                this.updateTableColumn(payment__Id, updateQuery);
            }
        } catch (Exception var21) {
            var21.printStackTrace();
            logger.info("Notification failed for " + payment__Id);
        }
    }

    private void sendToOtherBeneficiaries(String SQL, String sqlQuery, String ZenithEmail) {
        System.out.println("SQL : " + SQL);
        System.out.println("sqlQuery : " + sqlQuery);
        long payment__Id = 0;

        try {
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ResultSet reader = ps.executeQuery();

            while(reader.next()) {
                try {
                    LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                    String ref = reader.getString("Trans_ref");
                    rowsData.put("Your Ref", ref);
                    String beneName = reader.getString("vendor_name");
                    rowsData.put("Beneficiary Name", beneName);
                    String senderName = reader.getString("account_name");
                    rowsData.put("Sender Name", senderName);
                    String date = reader.getDate("payment_due_date").toString();
                    rowsData.put("Value Date", date);
                    rowsData.put("Currency", reader.getString("payment_currency"));
                    String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                    rowsData.put("Amount", amt);
                    String num = reader.getString("vendor_acct_no");
                    if (this.isValidString(num)) {
                        if(num.length() > 10){
                            num = num.substring(0, 10);
                        }
                        num = num.substring(0, 2) + "******" + num.substring(8);
                        rowsData.put("Beneficiary Account", num);
                    } else {
                        rowsData.put("Beneficiary Account", "BULK UPLOAD");
                    }
                    payment__Id = (long) reader.getDouble("payment_id");
                    rowsData.put("Payment ID", String.valueOf(payment__Id));
                    rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                    String purpose = reader.getString("vendor_category");
                    rowsData.put("Purpose", purpose);
                    String status = reader.getString("ptystatus");
                    rowsData.put("Status", status);
                    String vendorEmail = reader.getString("vendor_email");
                    if (this.isValidString(vendorEmail)) {
                        List<String> validEmail = GenerateReceipts.validateEmails(vendorEmail);
                        if (!vendorEmail.equalsIgnoreCase("") && null != vendorEmail && validEmail.size() > 0) {
                            boolean sent = GenerateReceipts.sendEmail(payment__Id, num, beneName, validEmail, rowsData, ref, amt, status, date, purpose, senderName);
                            if (sent) {
                                List<String> validEmailToZenith = GenerateReceipts.validateEmails(ZenithEmail);
                                GenerateReceipts.sendEmail(payment__Id, num, beneName, validEmailToZenith, rowsData, ref, amt, status, date, purpose, senderName);
                            } else {
                                logger.info(" payment_Id " + payment__Id + " was not sent.");
                           }
                        }
                    } else {
                        logger.info("Invalid Email address");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    logger.error("An unexpected error for payment with id " + payment__Id);
                }
                this.updateTableColumn(payment__Id, sqlQuery);
            }
        } catch (Exception var21) {
            var21.printStackTrace();
            logger.info("Notification failed for " + payment__Id);
        }

    }

    private void sendBulkDebitMail(String SQL, String sqlQuery, String ZenithEmail, long batchid){
        long payment__Id = 0;
        try{
            PreparedStatement ps = this.conn.prepareStatement(SQL);
            ps.setLong(1, batchid);
            ResultSet reader = ps.executeQuery();

            while (reader.next()){
                try {
                    LinkedHashMap<String, String> rowsData = new LinkedHashMap();
                    String ref = reader.getString("Trans_ref");
                    rowsData.put("Your Ref", ref);
                    String beneName = reader.getString("vendor_name");
                    rowsData.put("Beneficiary Name", beneName);
                    String senderName = reader.getString("account_name");
                    rowsData.put("Sender Name", senderName);
                    String date = reader.getDate("payment_due_date").toString();
                    rowsData.put("Value Date", date);
                    rowsData.put("Currency", reader.getString("payment_currency"));
                    String amt = GenerateReceipts.formatAmount(Double.toString(reader.getDouble("amount")));
                    rowsData.put("Amount", amt);
                    String num = reader.getString("vendor_acct_no");
                    if (this.isValidString(num)) {
                        if (num.length() > 10) {
                            num = num.substring(0, 10);
                        }
                        num = num.substring(0, 2) + "******" + num.substring(8);
                        rowsData.put("Beneficiary Account", num);
                    } else {
                        rowsData.put("Beneficiary Account", "");
                    }
                    payment__Id = (long) reader.getDouble("payment_id");
                    rowsData.put("Payment ID", String.valueOf(payment__Id));
                    rowsData.put("Beneficiary Bank", reader.getString("vendor_bank"));
                    String purpose = reader.getString("vendor_category");
                    rowsData.put("Purpose", purpose);
                    String status = reader.getString("ptystatus");
                    rowsData.put("Status", status);
                    String vendorEmail = reader.getString("vendor_email");
                    if (this.isValidString(vendorEmail)) {
                        List<String> validEmail = GenerateReceipts.validateEmails(vendorEmail);
                        if (!vendorEmail.equalsIgnoreCase("") && null != vendorEmail && validEmail.size() > 0) {
                            boolean sent = GenerateReceipts.sendEmail(payment__Id, num, beneName, validEmail, rowsData, ref, amt, status, date, purpose, senderName);
                            if (sent) {
                                List<String> validEmailToZenith = GenerateReceipts.validateEmails(ZenithEmail);
                                GenerateReceipts.sendEmail(payment__Id, num, beneName, validEmailToZenith, rowsData, ref, amt, status, date, purpose, senderName);
                            } else {
                                logger.info(" payment_Id " + payment__Id + " was not sent.");
                            }
                        }
                    } else {
                        logger.info("Invalid Email address");
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                this.updateBulkTableColumn(payment__Id, sqlQuery);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}