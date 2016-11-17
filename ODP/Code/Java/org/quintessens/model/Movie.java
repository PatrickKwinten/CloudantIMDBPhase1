package org.quintessens.model;

import java.io.Serializable;
import java.util.Date;

import lotus.domino.NotesException;
import org.quintessens.app.CloudantController;

public class Movie implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Date created;
	private String _id;
	private String _rev;
	
	public String get_rev() {
		return _rev;
	}
	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	private String name;
	
	private boolean newNote;
	private boolean readOnly;
	
	public Movie(){ 
		System.out.println(className() + " constructor");
	}	
	
	public void clear() {		
		created = null;
		name = null;		
	}
	
	public void create() throws NotesException{	
		setNewNote(true);
		setReadOnly(false);		
		CloudantController cloudant = new CloudantController();
		cloudant.connect();		
		Date date = new Date();
		this.created = date;		
		cloudant.saveDocument(this);
		this.clear();
	}
	
	private String className(){
		return this.getClass().getSimpleName().toString();
	}

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
		public boolean isNewNote() {
		return newNote;
	}
	public void setNewNote(boolean newNote) {
		this.newNote = newNote;
	}

	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}	
	
}
