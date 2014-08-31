package com.rugobal.dt.repository;

import static org.springframework.transaction.annotation.Isolation.DEFAULT;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * Base class for Repository tests. All repository tests should extend this class.
 * 
 * @author Ruben Gomez
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/dbTestsContext.xml")
@Transactional(isolation = DEFAULT, propagation = REQUIRED, rollbackFor = Exception.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractRepositoryTest  {

}