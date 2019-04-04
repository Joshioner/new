package com.blk.common.entity;


/***
 * 病历信息表
 *
 */
public class CaseHistory {
    //病历号
    private int cid;
    //病历原件
    private String photo;
    //就诊人
    private String visitName;
    //医院名称
    private String hospitalName;
    //科室
    private String office;
    //就诊医生
    private String doctorName;
    //就诊日期
    private String visitDate;
    //体格检查
    private String checkup;
    //诊断结果
    private String diagnosisResult;
    //主诉
    private String mainSuit;
    //现病史
    private String historyNow;
    //既往史
    private String historyPast;
    //过敏史
    private String historyAllergy;
    //家族史
    private String historyFamily;
    //婚育史
    private String historyMarriage;
    //处理意见
    private String suggestion;
    //操作时间
    private String operTime;
    //用户uid
    private int uid;
    //家庭成员id
    private int fid;

    public CaseHistory() {
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVisitName() {
        return visitName;
    }

    public void setVisitName(String visitName) {
        this.visitName = visitName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getCheckup() {
        return checkup;
    }

    public void setCheckup(String checkup) {
        this.checkup = checkup;
    }

    public String getDiagnosisResult() {
        return diagnosisResult;
    }

    public void setDiagnosisResult(String diagnosisResult) {
        this.diagnosisResult = diagnosisResult;
    }

    public String getMainSuit() {
        return mainSuit;
    }

    public void setMainSuit(String mainSuit) {
        this.mainSuit = mainSuit;
    }

    public String getHistoryNow() {
        return historyNow;
    }

    public void setHistoryNow(String historyNow) {
        this.historyNow = historyNow;
    }

    public String getHistoryPast() {
        return historyPast;
    }

    public void setHistoryPast(String historyPast) {
        this.historyPast = historyPast;
    }

    public String getHistoryAllergy() {
        return historyAllergy;
    }

    public void setHistoryAllergy(String historyAllergy) {
        this.historyAllergy = historyAllergy;
    }

    public String getHistoryFamily() {
        return historyFamily;
    }

    public void setHistoryFamily(String historyFamily) {
        this.historyFamily = historyFamily;
    }

    public String getHistoryMarriage() {
        return historyMarriage;
    }

    public void setHistoryMarriage(String historyMarriage) {
        this.historyMarriage = historyMarriage;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }
}
