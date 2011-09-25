package de.adesso.scalaspring.dao

import org.springframework.jdbc.core.JdbcTemplate
import de.adesso.scalaspring.domain.Customer
import de.adesso.scalaspring.jdbc.JdbcConversions._
import javax.sql.DataSource
import java.sql.ResultSet
import org.springframework.jdbc.core.RowMapper
import java.util.ArrayList
import collection.JavaConversions._
import scala.collection.mutable.Buffer
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

@Component
class CustomerDAO(dataSource: DataSource) {
  
  val jdbcTemplate = new JdbcTemplate(dataSource)

  def save(customer: Customer): Customer = {
    jdbcTemplate.update(
      "INSERT INTO CUSTOMER(FIRSTNAME,NAME,BALANCE) VALUES(?,?,?)",
      customer.firstname, customer.name, customer.balance: java.lang.Double)
    val id = jdbcTemplate.queryForInt("call identity()")
    Customer(id, customer.name, customer.firstname, customer.balance)
  }

  def deleteById(id: Integer) =
    jdbcTemplate.update("DELETE FROM CUSTOMER WHERE ID=?", id)
 

  def count(): Int = 
    jdbcTemplate.queryForInt("SELECT COUNT(*) FROM CUSTOMER")

  def findById(id: Integer): Option[Customer] = {
    val result: Buffer[Customer] = jdbcTemplate.query(
      "SELECT C.ID, C.NAME, C.FIRSTNAME, C.BALANCE FROM CUSTOMER C WHERE C.ID=?",
      (rs: ResultSet, rowNum: Int) => {
        Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4))
      },
      id)
    result.headOption
  }

}