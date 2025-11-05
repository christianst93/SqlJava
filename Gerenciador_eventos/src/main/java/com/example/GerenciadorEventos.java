package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GerenciadorEventos {
    public static void main(String[] args) {
       
        String host = "localhost"; 
        String porta = "5432";
        String nomeBanco = "gerenciador_eventos";
        String usuario = "postgres";
        String senha = "senha";
        
        String url = "jdbc:postgresql://" + host + ":" + porta + "/" + nomeBanco;

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha);
             Statement stmt = conexao.createStatement()) {           
           
            System.out.println("Conexão estabelecida com sucesso! O ambiente está pronto.");
        
        String inserirUsuario = "INSERT INTO usuario (nome,email, senha) VALUES (?,?,?) RETURNING id_usuario";
        int id_usuario;
        try (PreparedStatement ps = conexao.prepareStatement(inserirUsuario)){
            ps.setString(1, "Christian.Torres");
            ps.setString(2, "christian.ds.torres@gmail.com");
            ps.setString(3, "supersenha");
            ResultSet rs = ps.executeQuery();
            rs.next();
            id_usuario = rs.getInt("id_usuario");
            System.out.println("Usuário cadastrado com ID: " + id_usuario);
        }

        String inserirEvento = "INSERT INTO evento (titulo, descricao, data_evento, local, id_usuario, criado_em) VALUES (?,?,?,?,?,?) RETURNING id_evento";
        int id_evento;
        try (PreparedStatement ps = conexao.prepareStatement(inserirEvento)){
            ps.setString(1, "Campeonato de Boxe");
            ps.setString(2, "Campeonato com 5 categoris separadas por peso, valendo o titulo de Campeão Municipal");
            ps.setDate(3, java.sql.Date.valueOf("2025-10-25"));
            ps.setString(4, "Ginasio do AABB");
            ps.setInt(5, id_usuario);
            ps.setTimestamp(6, java.sql.Timestamp.valueOf("2025-09-10 18:23:56"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            id_evento = rs.getInt("id_evento");
            System.out.println("Evento cadastrado com ID: " + id_evento);
        }

        String inserirParticipante = "INSERT INTO participante (nome, email, telefone, data_cadastro) VALUES (?,?,?,?) RETURNING id_participante";
        int id_participante;
        try (PreparedStatement ps = conexao.prepareStatement(inserirParticipante)) {
            ps.setString(1, "Arthur Ferreira");
            ps.setString(2, "arthur.ferreira@gmail.com");
            ps.setString(3, "(53)99987-2354");
            ps.setTimestamp(4, java.sql.Timestamp.valueOf("2025-10-01 14:34:21"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            id_participante = rs.getInt("id_participante");
            System.out.println("Participante cadastrado com ID: " + id_participante);
        }

        String inserirInscricao = "INSERT INTO inscricao (id_participante, id_evento, data_inscricao) VALUES (?,?,?) RETURNING id_inscricao";
        int id_inscricao;
        try (PreparedStatement ps = conexao.prepareStatement(inserirInscricao)) {
            ps.setInt(1, id_participante);
            ps.setInt(2, id_evento);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf("2025-10-09 10:43:12"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            id_inscricao = rs.getInt("id_inscricao");
            System.out.println("Inscrição realizada com ID: " + id_inscricao);
        }

            
        } catch (SQLException e) {           
            System.err.println("Falha de Conexão ou Erro SQL.");
            System.err.println("Mensagem: " + e.getMessage());            
            
            if (e.getMessage().contains("password authentication failed")) {
                System.err.println("Verifique se a senha de '" + usuario + "' no código está correta.");
            } else if (e.getMessage().contains("database \"gerenciador_eventos\" does not exist")) {
                 System.err.println("O banco de dados não existe.");
            }
        } 
    }
}
