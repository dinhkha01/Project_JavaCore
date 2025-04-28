package business.dao.admin.applicationForm;

import business.dao.IGenericDao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface IApplication extends IGenericDao {
    List<Map<String, Object>> filterByProgress(String progress);
    List<Map<String, Object>> filterByResult(String result);
    boolean cancelApplication(int appId, String reason);
    boolean sendInterviewInfo(int appId, String interviewLink, Timestamp interviewTime);
    boolean updateInterviewResult(int appId, String result, String note);
}