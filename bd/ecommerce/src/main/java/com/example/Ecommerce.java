package com.example;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;    
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;



public class Ecommerce
{
    public static void main( String[] args )
    {
        String host = "localhost";
        String usuario = "postgres";
        String porta = "5432";
        String senha = "senha";
        String nomeBanco = "e_commerce";

        String url = "jdbc:postgresql://"+host+":"+porta+"/"+nomeBanco;

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conectado ao Banco e_commerce!");
        
        String sqlCliente = "INSERT INTO cliente (nome, cpf, email, telefone, data_nascimento, data_cadastro)" + 
        "VALUES (?,?,?,?,?,?) RETURNING id_cliente";
        int id_cliente = 0;
        try (PreparedStatement stmt = conexao.prepareStatement(sqlCliente)) {
            stmt.setString(1, "Christian");
            stmt.setString(2, "012.345.678-90");
            stmt.setString(3, "christian.torres@gmail.com");
            stmt.setString(4, "(53) 99912-3456");
            
            LocalDate data_nascimento = LocalDate.of(1993, 2, 15);
            stmt.setDate(5, Date.valueOf(data_nascimento));
            
            LocalDateTime data_cadastro = LocalDateTime.now();
            stmt.setTimestamp(6, Timestamp.valueOf(data_cadastro));

            try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        id_cliente = rs.getInt("id_cliente");
                        System.out.println("Cliente inserido! Id: " + id_cliente);
                    }
                }
            }
        
        String sqlCategoria = "INSERT INTO categoria (nome, descricao) VALUES (?,?) RETURNING id_categoria";
        int id_categoria = 0;
        try (PreparedStatement stmt = conexao.prepareStatement(sqlCategoria)) {
            stmt.setString(1, "Smartphones");
            stmt.setString(2, "Dispositivos móveis com tela touch de alta resolução e sistema operacional Android ou iOS.");
            
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id_categoria = rs.getInt("id_categoria");
                    System.out.println("Categoria adicionada! Id:" + id_categoria);
                }
            }
        }

        String sqlProduto = "INSERT INTO produto (nome, descricao, preco, estoque, id_categoria, ativo, criado_em)" + 
        "VALUES (?,?,?,?,?,?,?) RETURNING id_produto";
        int id_produto = 0;
        int estoque = 0;
        try (PreparedStatement stmt = conexao.prepareStatement(sqlProduto)) {
            stmt.setString(1, "Iphone 17");
            stmt.setString(2, "256GB Preto com chip A19 e camera fusion de 48MP.");
            
            BigDecimal precoProduto = new BigDecimal("7999.00");
            stmt.setBigDecimal(3, precoProduto );

            estoque = 7;
            stmt.setInt(4, estoque);
            stmt.setInt(5, id_categoria);
            stmt.setBoolean(6, true);

            Instant instant = Instant.now();
            stmt.setTimestamp(7, Timestamp.from(instant));

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id_produto = rs.getInt("id_produto");
                    System.out.println("Produto adicionado! Id:" + rs.getInt("id_produto"));
                }
            }
        }

        String sqlPedido = "INSERT INTO pedido (id_cliente, data_pedido, status,valor_total, observacoes)" +
        "VALUES (?,?,?,?,?) RETURNING id_pedido";
        int id_pedido = 0;
        BigDecimal valor_total = BigDecimal.ZERO;
        try(PreparedStatement stmt = conexao.prepareStatement(sqlPedido)) {
            stmt.setInt(1, id_cliente);

            LocalDate data_pedido = LocalDate.of(2025, 10 ,20);
            stmt.setDate(2, Date.valueOf(data_pedido));

            stmt.setString(3, "Pago");
            stmt.setBigDecimal(4, valor_total);            
            stmt.setString(5, "Entregar somente a partir das 14h, porteiro autorizado a receber.");

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id_pedido = rs.getInt("id_pedido");
                    System.out.println("Pedido adicionado! Id:" + id_pedido);
                }
            }
        
        }

        String sqlItemPedido = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_unitario, subtotal)" + 
        "VALUES (?,?,?,?,?) RETURNING id_item";
        int id_item = 0;
        int quantidade = 0; 
        BigDecimal subtotal = BigDecimal.ZERO;       
        try (PreparedStatement stmt = conexao.prepareStatement(sqlItemPedido)) {
            quantidade = 3;
            stmt.setInt(1, id_pedido);
            stmt.setInt(2, id_produto);
            stmt.setInt(3, quantidade);
            
            BigDecimal preco_unitario = new BigDecimal("43422.00");
            stmt.setBigDecimal(4, preco_unitario);

            subtotal = preco_unitario.multiply(BigDecimal.valueOf(quantidade));
            stmt.setBigDecimal(5, subtotal);


        try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    id_item = rs.getInt("id_item");
                    System.out.println("Item do pedido adicionado! Id:" + id_item);
                }
            }
        }

        String sqlValor_total = "UPDATE pedido SET valor_total = ? WHERE id_pedido = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sqlValor_total)) {            
            stmt.setBigDecimal(1, subtotal);
            stmt.setInt(2, id_pedido);

            stmt.executeUpdate();
        }

        int novo_estoque = estoque - quantidade; 
        
        String sqlUpdateEstoque = "UPDATE produto SET estoque = ? WHERE id_produto = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sqlUpdateEstoque)) {
            
            stmt.setInt(1, novo_estoque);
            stmt.setInt(2, id_produto);
            
            stmt.executeUpdate();
        }

        

        } catch (SQLException e) {            
            e.printStackTrace();
        }

        
    }
}
