package com.spring.tacocloud.data;

import com.spring.tacocloud.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class jdbcIngredientRepository implements IngredientRepository{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public jdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {

        String sql = "select id,name,type from Ingredient ";

        //匿名内部类的RowMapper
        return jdbcTemplate.query(sql, new RowMapper<Ingredient>() {
            @Override
            public Ingredient mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Ingredient(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        Ingredient.Type.valueOf(resultSet.getString("type"))
                );
            }
        });
    }

    @Override
    public Ingredient findOneById(String id) {
        String sql = "select id,name,type from Ingredient where id=?";

        //Java8的方法引用或lambda表达式
        return jdbcTemplate.queryForObject(sql,this::rowMapperToIngredient,id);
    }

    private Ingredient rowMapperToIngredient(ResultSet resultSet, int rowNum) throws SQLException {
        return new Ingredient(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Ingredient.Type.valueOf(resultSet.getString("type"))
                );
    }


    @Override
    public Ingredient save(Ingredient ingredient) {

        String sql = "insert into Ingredient (id,name,type) values (?, ?, ?)";

        jdbcTemplate.update(sql,ingredient.getId(),ingredient.getName(),ingredient.getType().toString());
        return ingredient;
    }
}
