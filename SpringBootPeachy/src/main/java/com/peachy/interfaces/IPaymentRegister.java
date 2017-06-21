package com.peachy.interfaces;

import com.peachy.entity.PaymentRegister;

public interface IPaymentRegister {
	public void create(PaymentRegister paymentRegister);
	public PaymentRegister retrieve(int entryId);
	public void update(PaymentRegister paymentRegister);
	public void delete(PaymentRegister paymentRegister);
}
