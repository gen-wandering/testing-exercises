package com.testingexercises.repository;

import com.testingexercises.exceptions.CustomerNotFoundException;
import com.testingexercises.exceptions.DuplicateEmailException;
import com.testingexercises.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Customer> customerRowMapper = (rs, rowNum) -> new Customer(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email")
    );

    private boolean foundById(int id) {
        String idSql = """
                SELECT *
                FROM testing.customers
                WHERE id = ?;
                """;
        return !jdbcTemplate.query(idSql, customerRowMapper, id).isEmpty();
    }

    private boolean notFoundByEmail(String email) {
        String emailSql = """
                SELECT count(*)
                FROM testing.customers
                WHERE email = ?;
                """;
        Integer amount = jdbcTemplate.queryForObject(emailSql, Integer.class, email);
        return amount != null && amount == 0;
    }

    public Iterable<Customer> getCustomers() {
        String sql = """
                SELECT *
                FROM testing.customers;
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    public Customer getCustomer(int customerId) {
        String sql = """
                SELECT *
                FROM testing.customers
                WHERE id = ?;
                """;
        var res = jdbcTemplate.query(sql, customerRowMapper, customerId);

        if (!res.isEmpty())
            return res.get(0);
        else
            throw new CustomerNotFoundException("No customer with id=" + customerId);
    }

    public boolean addNewCustomer(Customer customer) {
        String sql = """
                INSERT INTO testing.customers (first_name, last_name, email)
                VALUES (?, ?, ?);
                """;
        if (notFoundByEmail(customer.getEmail())) {
            jdbcTemplate.update(sql,
                    customer.getName(),
                    customer.getSurname(),
                    customer.getEmail()
            );
            return true;
        } else
            throw new DuplicateEmailException("Duplicate email=" + customer.getEmail());
    }

    public boolean updateCustomer(Customer customer) {
        String update = "UPDATE testing.customers SET ";
        String where = " WHERE id=";
        StringBuilder fieldsToUpdate = new StringBuilder();

        if (foundById(customer.getId())) {
            if (customer.getName() != null) {
                fieldsToUpdate.append("first_name=")
                        .append("'")
                        .append(customer.getName())
                        .append("'")
                        .append(", ");
            }
            if (customer.getSurname() != null) {
                fieldsToUpdate.append("last_name=")
                        .append("'")
                        .append(customer.getSurname())
                        .append("'")
                        .append(", ");
            }
            if (customer.getEmail() != null) {
                fieldsToUpdate.append("email=")
                        .append("'")
                        .append(customer.getEmail())
                        .append("'")
                        .append(", ");
            }
            String fields = fieldsToUpdate.substring(0, fieldsToUpdate.length() - 2);

            String sql = update + fields + where + customer.getId() + ";";
            jdbcTemplate.update(sql);
            return true;
        } else
            throw new CustomerNotFoundException("No customer with id=" + customer.getId());
    }

    public boolean deleteCustomer(int customerId) {
        String sql = """
                DELETE FROM testing.customers
                WHERE id = ?;
                """;
        if (foundById(customerId)) {
            jdbcTemplate.update(sql, customerId);
            return true;
        } else
            throw new CustomerNotFoundException("No customer with id=" + customerId);
    }
}