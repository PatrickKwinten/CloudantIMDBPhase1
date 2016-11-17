package org.quintessens.app;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.quintessens.model.Movie;

import ch.belsoft.tools.XPagesUtil;

import nl.elstarit.cloudant.connector.CloudantConnector;
import nl.elstarit.cloudant.log.CloudantLogger;
import nl.elstarit.cloudant.model.ConnectorIndex;
import nl.elstarit.cloudant.model.ConnectorResponse;

public class CloudantController {
	
    private String account;
    private String password;
    private String username;
    private String dbName;
    private boolean connected;   
    private XPagesUtil xpagesUtil;         
    private CloudantConnector connector;
	
	public CloudantController(){
		System.out.println("constructor cloudantcontroller");
	}	
	
	public void connect(){		
		if (xpagesUtil == null) {
			xpagesUtil = new XPagesUtil();
			setUsername(xpagesUtil.getLangString("cloudant.properties", "username", ""));			
			setPassword(xpagesUtil.getLangString("cloudant.properties", "password", ""));			
			setAccount(xpagesUtil.getLangString("cloudant.properties", "account", ""));	
			setDbName(xpagesUtil.getLangString("cloudant.properties", "database", ""));
		}
		      
        connector = new CloudantConnector();
        connector.initCloudantClientAdvanced(account, username, password, dbName, false, 1, 1, TimeUnit.MINUTES); 
        connected = true;
	}	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getDbName() {
		return dbName;
	}



	public void setDbName(String dbName) {
		this.dbName = dbName;
	}



	public boolean isConnected() {
		return connected;
	}

	public  void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	   /*
     * Database connectors
     */
    
    public void switchDatabase(String db, boolean create) {
        connector.switchDatabase(db, create);
    }
    
    public void createDatabase(String db) {
        connector.switchDatabase(db, true);
    }
    
    /*
     * Document connectors
     */
    
    public Object findDocumentByID(Class<?> cls, String documentId) {
        try {
            return connector.getDocumentConnector().find(cls, documentId);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    public List<?> findAllDocuments(Class<?> cls) {
        try {
            return connector.getDocumentConnector().findAllDocuments(cls);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    public void removeDocument(Object obj) {
        try {
            connector.getDocumentConnector().delete(obj);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public void removeAllDocuments(){
    	this.connect();
    	System.out.println("removeAllDocuments");
    	try{
    		List<Movie> docs = (List<Movie>) this.findAllDocuments(Movie.class);
    		System.out.println(docs.size());
    		this.deleteDocuments(docs);
    	}catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public ConnectorResponse saveDocument(Object obj) {
        ConnectorResponse resp = null;
        try {
            resp = connector.getDocumentConnector().save(obj);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return resp;
    }
    
    public ConnectorResponse updateDocument(Object obj) {
        ConnectorResponse resp = null;
        try {
            resp = connector.getDocumentConnector().update(obj);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return resp;
    }
    
    
    
    public void saveDocuments(final List<?> docs) {
        try {
            connector.getDocumentConnector().createBulk(docs);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
            
           
        }
    }
    
    public void updateDocuments(final List<?> docs) {
        try {
            connector.getDocumentConnector().updateBulk(docs);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public void deleteDocuments(final List<?> docs) {
        try {
            connector.getDocumentConnector().deleteBulk(docs);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
    }
    
    public ConnectorResponse saveStandaloneAttachment(final InputStream inputStream,
            final String name, final String contentType, final String docId,
            final String docRev) {
        try {
            return connector.getDocumentConnector().saveStandAloneAttachment(inputStream,
                    name, contentType, docId, docRev);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    public void createDesignDoc(){
    	this.connect();
    	try {
        	HashMap<String, String> views = new HashMap<String, String>();
        	views.put("map", "function (doc) {\n  if(doc._id ){\n    emit(doc._id, 1);\n  }\n}");
            connector.getDocumentConnector().createDesignDocument(views, "_design/default");
        } catch (Exception e) {
            //CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,e.getMessage());
            System.out.println(e.getMessage());
        }        
    }
    
    
    /**
     * 
     * @param cls
     * @param designDoc
     * @param viewName
     * @param keyType
     * @param limit
     * @return
     */
    public List<?> findAllDocumentFromView(final Class<?> cls,
            final String designDoc, final String viewName,
            final String keyType, final int limit) {
        try {
            return connector.getDocumentConnector().findAllDocumentsFromView(
                    cls, designDoc, viewName, keyType, limit);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    /**
     * 
     * @param cls
     * @param designDoc
     * @param viewName
     * @param keyType
     * @param limit
     * @param startKey
     * @param endKey
     * @return
     */
    public List<?> findAllDocumentFromViewKeys(final Class<?> cls,
            final String designDoc, final String viewName,
            final String keyType, final int limit, final String startKey,
            final String endKey) {
        try {
            return connector.getDocumentConnector()
            .findAllDocumentsFromViewKeys(cls, designDoc, viewName,
                    keyType, limit, startKey, endKey);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }
    
    /*
     * Query
     */
    
    public List<ConnectorIndex> allIndices() {
        return (List<ConnectorIndex>) connector.getQueryConnector().allIndices();
    }
    
    public List<?> search(final String searchIndexId, final Class<?> cls,
            final Integer queryLimit, final String query) {        
        try {
            return connector.getQueryConnector().search(searchIndexId, cls,
                    queryLimit, query);
        } catch (Exception e) {
            CloudantLogger.CLOUDANT.getLogger().log(Level.SEVERE,
                    e.getMessage());
        }
        return null;
    }

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}	
	
}