package com.peachy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.SchemesDao;
import com.peachy.entity.Schemes;
import com.peachy.interfaces.ISchemes;

@Service
public class SchemesService implements ISchemes {

	@Autowired
	SchemesDao schemesDao;
	
	@Override
	public void create(Schemes schemes) {
		schemesDao.create(schemes);
	}

	@Override
	public Schemes retrieve(int entry_id) {
		return schemesDao.retrieve(entry_id);
	}

	@Override
	public List<Schemes> retrieveScheme(String scheme) {
		return schemesDao.retrieveScheme(scheme);
	}
	
	public List<String> retrieveAllSchemes() {
		return schemesDao.retrieveAllSchemes();
	}

	@Override
	public void update(Schemes schemes) {
		schemesDao.update(schemes);
	}

	@Override
	public void delete(Schemes schemes) {
		schemesDao.delete(schemes);
	}
	
	public void delete(String scheme) {
		schemesDao.delete(scheme);
	}
	
	public void delete(int entry_id) {
		Schemes schemes = retrieve(entry_id);
		delete(schemes);
	}
	
	public boolean idExists(String scheme, String id) {
		return schemesDao.idExists(scheme, id);
	}

}
