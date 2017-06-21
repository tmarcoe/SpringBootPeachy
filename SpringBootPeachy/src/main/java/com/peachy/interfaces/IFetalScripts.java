package com.peachy.interfaces;

import com.peachy.entity.FetalScripts;

public interface IFetalScripts {
	
	public void create(FetalScripts fetalScripts);
	public FetalScripts retrieve(int id);
	public void update(FetalScripts fetalScripts);
	public void delete(FetalScripts fetalScripts);
}
