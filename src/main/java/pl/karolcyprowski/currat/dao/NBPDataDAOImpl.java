package pl.karolcyprowski.currat.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;

import pl.karolcyprowski.currat.entities.ExchangeRate;
import pl.karolcyprowski.currat.entities.ExchangeRatePrimaryKey;

@Repository
@Transactional
public class NBPDataDAOImpl implements NBPDataDAO {
	
	static Logger logger = Logger.getLogger(NBPDataDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	public NBPDataDAOImpl() {
		
	}
	
	public ExchangeRate getExchangeRate(Date date, String code) {
		try {
			Session currentSession = sessionFactory.getCurrentSession();
			ExchangeRatePrimaryKey key = new ExchangeRatePrimaryKey(code, date);
			ExchangeRate exchangeRate = currentSession.get(ExchangeRate.class, key);
			return exchangeRate;
		} catch (JpaSystemException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<ExchangeRate> getExchangeRates(Date startDate, Date endDate, String code) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("from ExchangeRate where currency_code='" + code + "' and date between '" + startDate + "' and '" + endDate + "'");
		return query.list();
	}
	
	public List<ExchangeRate> getExchangeRates(Date date) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("from ExchangeRate where date='" + date + "'");
		return query.list();
	}
	
	public List<ExchangeRate> getExchangeRates(Date startDate, Date endDate){
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("from ExchangeRate where date between '" + startDate + "' and '" + endDate + "'");
		return query.list();
	}
	
	public List<ExchangeRate> getExchangeRates(String code) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("from ExchangeRate where currency_code='" + code + "'");
		logger.info(query);
		return query.list();
	}
	
	public void insertData(List<ExchangeRate> exchangeRates) {
		long startTime = System.currentTimeMillis();
		StatelessSession currentSession = sessionFactory.openStatelessSession();
		Transaction tx = currentSession.beginTransaction();
		int failedInsertCount = 0;
		for (int i = 0; i < exchangeRates.size(); i++) {
			ExchangeRate exchangeRate = exchangeRates.get(i);
			try {
				currentSession.insert(exchangeRate);
			} catch (Exception e) {
				failedInsertCount++;
			}			
		}
		tx.commit();
		currentSession.close();
		logger.info(exchangeRates.size() + " items inserted in " + (System.currentTimeMillis() - startTime) + " ms");
		if(failedInsertCount > 0) {
			logger.warn(failedInsertCount + " inserts have failed.");
		}
	}
	
	private void insertData(ExchangeRate exchangeRate) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(exchangeRate);
	}

	
	
}
