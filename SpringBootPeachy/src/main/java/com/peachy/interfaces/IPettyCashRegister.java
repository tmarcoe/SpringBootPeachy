package com.peachy.interfaces;

import com.peachy.entity.PettyCashRegister;

public interface IPettyCashRegister {
	public void create(PettyCashRegister pettyCashRegister);
	public PettyCashRegister retrieve(int registerId);
	public void update(PettyCashRegister pettyCashRegister);
	public void delete(PettyCashRegister pettyCashRegister);
}
