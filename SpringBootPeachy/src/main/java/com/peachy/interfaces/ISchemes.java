package com.peachy.interfaces;

import java.util.List;

import com.peachy.entity.Schemes;

public interface ISchemes {
	public void create(Schemes schemes);
	public Schemes retrieve(int entry_id);
	public List<Schemes> retrieveScheme(String scheme);
	public void update(Schemes schemes);
	public void delete(Schemes schemes);

}
