package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DbUtil;
import com.util.Error.PriceProposalNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by Sang on 15/10/21.
 */
@Repository
public class PriceProposalDao {



    public double[] getPriceByCode(String code, String customerId) throws SQLException{
        int proposalId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = DbUtil.getCon();
        try {
            if (customerId != null) {
                String sql = "SELECT price_proposal_id FROM tb_customer WHERE id = ?";
                ps = connection.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(customerId));
                rs = ps.executeQuery();
                while (rs.next()) {
                    proposalId = rs.getInt("price_proposal_id");
                }
            }
            String sql2 = "SELECT price,cost  FROM tb_product_fix WHERE id IN\n" +
                    "(SELECT product_fix_id FROM tb_mapping02 WHERE price_proposal_id = ?) AND\n" +
                    "code = ?";
            ps = connection.prepareStatement(sql2);
            ps.setInt(1, proposalId);
            ps.setString(2, code);
            rs = ps.executeQuery();
            double price = 1000000;
            double cost = 0;
            while (rs.next()) {
                price = rs.getDouble("price");
                cost = rs.getDouble("cost");
            }
            return new double[]{price, cost};
        } catch (SQLException e) {
            e.printStackTrace();
            return new double[]{100000, 0};
        } finally {
            connection.close();
        }
    }

    public double[] getPrice(String paper, String side, int amount, String customer)throws SQLException,PriceProposalNotFoundException{
        int proposalId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = DbUtil.getCon();
        try {
            if (customer != null) {
                String sql = "SELECT price_proposal_id FROM tb_customer WHERE user_name = ?";
                ps = connection.prepareStatement(sql);
                ps.setString(1, customer);
                rs = ps.executeQuery();
                while (rs.next()) {
                    proposalId = rs.getInt("price_proposal_id");
                }
            }
            String sql2 = "SELECT price,cost FROM tb_product_fix WHERE id IN\n" +
                    "(SELECT product_fix_id FROM tb_mapping02 WHERE price_proposal_id = ?) AND\n" +
                    "paper = ? AND side=? AND amount=? AND size_unit=1";
            ps = connection.prepareStatement(sql2);
            ps.setInt(1, proposalId);
            ps.setString(2, paper);
            ps.setString(3, side);
            ps.setInt(4, amount);
            rs = ps.executeQuery();
            double price = 1000000;
            double cost = 0;
            while (rs.next()) {
                price = rs.getDouble("price");
                cost = rs.getDouble("cost");
            }
            if(price == 1000000){
                throw new PriceProposalNotFoundException();
            }
            return new double[]{price, cost};
        }catch (SQLException e){
            throw new SQLException();
        }finally {
            connection.close();
        }
    }

}
