package com.peachy.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.peachy.entity.Inventory;
import com.peachy.entity.InvoiceItem;
import com.peachy.interfaces.IInventory;

@Transactional
@Repository
public class InventoryDao implements IInventory {

	@Autowired
	SessionFactory sessionFactory;

	Session session() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void create(Inventory inventory) {
		Session session = session();
		Transaction tx = session.beginTransaction();

		session.save(inventory);
		tx.commit();
	}

	@Override
	public Inventory retrieve(String sku_num) {
		Session session = session();
		Inventory inv = (Inventory) session.createCriteria(Inventory.class).add(Restrictions.idEq(sku_num)).uniqueResult();
		session.disconnect();;
		return 	inv;
	}

	@Override
	public void update(Inventory inventory) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		session.update(inventory);
		tx.commit();
		session.disconnect();;
	}

	@Override
	public void delete(Inventory inventory) {
		Session session = session();
		Transaction tx = session.beginTransaction();
		String hql = "DELETE FROM Inventory WHERE sku_num = :sku_num";
		session.createQuery(hql).setString("sku_num", inventory.getSku_num()).executeUpdate();
		tx.commit();
		session.disconnect();;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> listSaleItems() {
		Session session = session();
		String hql = "FROM Inventory WHERE on_sale is true";
		List<Inventory> invList = session.createQuery(hql).setMaxResults(3).list();
		
		return invList;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> searchProducts(String searchStr) {
		Session session = session();
		String hql = "FROM Inventory WHERE lower(product_name) LIKE lower(\'%" + searchStr
				+ "%\') OR lower(description) LIKE lower(\'%" + searchStr + "%\') "
				+ "OR lower(category) LIKE lower(\'%" + searchStr + "%\') "
				+ "OR lower(subcategory) LIKE lower(\'%" + searchStr + "%\') ";
		List<Inventory> searchList = session.createQuery(hql).list();
		session.disconnect();;

		return searchList;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> listProducts() {
		Session session = session();
		Criteria crit = session.createCriteria(Inventory.class);
		List<Inventory> inv = crit.list();
		session.disconnect();;

		return inv;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> listProducts(String category) {
		Session session = session();
		Criteria crit = session.createCriteria(Inventory.class);
		crit.add(Restrictions.eq("category", category));
		List<Inventory> inv = crit.list();
		session.disconnect();;

		return inv;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> listProducts(String category, String subCategory) {
		Session session = session();
		Criteria crit = session.createCriteria(Inventory.class);
		crit.add(Restrictions.eq("category", category));
		crit.add(Restrictions.eq("subcategory", subCategory));
		List<Inventory> inv = crit.list();
		session.disconnect();;
		
		return inv;
	}
	@SuppressWarnings("unchecked")
	public List<Inventory> listAllProducts() {
		Session session = session();
		List<Inventory> invList = session.createCriteria(Inventory.class).list();
		session.disconnect();
		
		return invList;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> getCategory() {
		Session session = session();
		String hql = "select DISTINCT category, sku_num, product_name, subcategory, amt_in_stock, amt_committed,"
				+ " min_quantity, sale_price, discount_price, tax_amt, weight, on_sale, image, description FROM Inventory GROUP BY category";
		List<Inventory> cat = session.createQuery(hql).list();
		session.disconnect();
		
		return cat;
	}

	@SuppressWarnings("unchecked")
	public List<Inventory> getSubCategory(String category) {
		Session session = session();
		String hql = "select DISTINCT subcategory, sku_num, product_name, category, amt_in_stock, amt_committed,"
				+ " min_quantity, sale_price, discount_price, tax_amt, weight, on_sale, image, description"
				+ "  FROM Inventory where category = :category GROUP BY subcategory";

		List<Inventory> subCat = session.createQuery(hql).setString("category", category).list();
		session.disconnect();
		
		return subCat;
	}

	public void depleteInventory(List<InvoiceItem> invoiceItems) {
		String hql = "UPDATE Inventory SET amt_committed = (amt_committed - :amount) WHERE sku_num = :sku_num";
		Session session = session();
		Transaction tx = session.beginTransaction();
		for (InvoiceItem item : invoiceItems) {
			session.createQuery(hql).setInteger("amount", Integer.valueOf(String.valueOf(item.getAmount())))
									.setString("sku_num", item.getSku_num()).executeUpdate();
		}
		tx.commit();
		session.disconnect();
	}
	@SuppressWarnings("unchecked")
	public List<Inventory> getReplenishList() {
		Session session = session();
		String hql = "from Inventory where amt_in_stock < min_quantity";
		List<Inventory> inv = session.createQuery(hql).list();

		session.disconnect();
		return inv;
	}

}
