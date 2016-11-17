package org.quintessens.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lotus.domino.NotesException;

import org.quintessens.app.CloudantController;
import org.quintessens.model.Movie;

import ch.belsoft.tools.XPagesUtil;

public class MovieController implements Serializable{

	private static final long serialVersionUID = 1L;	
	private List<Movie> movies = new ArrayList<Movie>();
		
	@SuppressWarnings("unchecked")
	public List<Movie> getMovies() throws NotesException{
		CloudantController cloudant = new CloudantController();
		new XPagesUtil();		
		cloudant.connect();	
		movies = (List<Movie>) cloudant.findAllDocuments(Movie.class);
		return movies;		
	}
	
	public void remove(String id){
		CloudantController cloudant = new CloudantController();
		cloudant.connect();	
		Movie movie = (Movie) cloudant.findDocumentByID(Movie.class, id);
		cloudant.removeDocument(movie);	
	}
	
	public void update(String id, String newName, Integer newEmployees){
		CloudantController cloudant = new CloudantController();
		cloudant.connect();	
		Movie movie = (Movie) cloudant.findDocumentByID(Movie.class, id);
		movie.setName(newName);		
		cloudant.updateDocument(movie);
	}
	
}
