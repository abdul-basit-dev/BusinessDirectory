package com.social.businessdirectory.Model;

public class CompanyModel {
    String companyName, firstName, lastName, mobile, landline, email, website, businessCard, adress1, adress2, pincode;
    String city, state, country, status;
    String cMobile, cLandLine, cEmail, cWebsite, cWholeSale, cRetails, cTerms;

    String postID;

    String categoryName, businessDisc;


    public CompanyModel(String categoryName, String businessDisc, String postID, String companyName, String firstName, String lastName, String mobile, String landline, String email, String website, String businessCard, String adress1, String adress2, String pincode, String city, String state, String country, String status, String cMobile, String cLandLine, String cEmail, String cWebsite, String cWholeSale, String cRetails) {
        this.companyName = companyName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.landline = landline;
        this.email = email;
        this.website = website;
        this.businessCard = businessCard;
        this.adress1 = adress1;
        this.adress2 = adress2;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.status = status;
        this.cMobile = cMobile;
        this.cLandLine = cLandLine;
        this.cEmail = cEmail;
        this.cWebsite = cWebsite;
        this.cWholeSale = cWholeSale;
        this.cRetails = cRetails;
        this.postID = postID;
        this.categoryName = categoryName;
        this.businessDisc = businessDisc;

    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(String businessCard) {
        this.businessCard = businessCard;
    }

    public String getAdress1() {
        return adress1;
    }

    public void setAdress1(String adress1) {
        this.adress1 = adress1;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getcMobile() {
        return cMobile;
    }

    public void setcMobile(String cMobile) {
        this.cMobile = cMobile;
    }

    public String getcLandLine() {
        return cLandLine;
    }

    public void setcLandLine(String cLandLine) {
        this.cLandLine = cLandLine;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcWebsite() {
        return cWebsite;
    }

    public void setcWebsite(String cWebsite) {
        this.cWebsite = cWebsite;
    }

    public String getcWholeSale() {
        return cWholeSale;
    }

    public void setcWholeSale(String cWholeSale) {
        this.cWholeSale = cWholeSale;
    }

    public String getcRetails() {
        return cRetails;
    }

    public void setcRetails(String cRetails) {
        this.cRetails = cRetails;
    }

    public String getcTerms() {
        return cTerms;
    }

    public void setcTerms(String cTerms) {
        this.cTerms = cTerms;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBusinessDisc() {
        return businessDisc;
    }

    public void setBusinessDisc(String businessDisc) {
        this.businessDisc = businessDisc;
    }
}
