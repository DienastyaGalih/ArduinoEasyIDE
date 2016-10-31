/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author User
 */
public class Project {
    String projectName;
    int projectId;
    String dateModified;
    String dateCreated;

    public Project(String projectName, int projectId, String dateModified, String dateCreated) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.dateModified = dateModified;
        this.dateCreated = dateCreated;
    }
    
    
    
}
