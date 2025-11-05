package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Sistema_Cursos 
{
    public static void main( String[] args )
    {
        String host = "localhost";
        String usuario = "postgres";
        String senha = "senha";
        String porta = "5432";
        String banco = "sistema_cursos";

        String url = "jdbc:postgresql://"+host+":"+porta+"/"+banco;

        Scanner in = new Scanner(System.in);

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha)){
                    System.out.println("Conectado!");        

        do {
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Cadastrar instrutor");
            System.out.println("3 - Cadastrar curso");
            System.out.println("4 - Cadastrar matrícula");
            System.out.println("0 - Sair");

            int opcao = in.nextInt();
            in.nextLine();

            switch (opcao) {
            case 0: return; 
            case 1:  String sqlAluno = "Insert into aluno (nome_aluno, email_aluno, senha_aluno, data_nascimento, data_registro_aluno)" + 
                    " VALUES (?,?,?,?,?) RETURNING id_aluno";
                    int id_aluno;
                    try (PreparedStatement stms = conexao.prepareStatement(sqlAluno)){
                        System.out.println("Digite o nome do aluno:");
                        stms.setString(1, in.nextLine());

                        System.out.println("Digite o email do aluno:");
                        stms.setString(2, in.nextLine());

                        System.out.println("Digite a senha");
                        stms.setString(3, in.nextLine());

                        System.out.println("Digite a data de nascimento:");
                        String data_nascimento = in.nextLine();                
                        stms.setDate(4, java.sql.Date.valueOf(data_nascimento));

                        System.out.println("Digite a data do registro");
                        String data_registro_aluno = in.nextLine();
                        stms.setTimestamp(5, java.sql.Timestamp.valueOf(data_registro_aluno));
                        
                        ResultSet rs = stms.executeQuery();
                        rs.next();
                        id_aluno = rs.getInt("id_aluno");
                        System.out.println("\nAluno cadastrado! ID:" +id_aluno+"\n");
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;

            case 2: String sqlInstrutor = "INSERT INTO instrutor " + 
                    "(nome_instrutor, email_instrutor, senha_instrutor, descricao_instrutor, data_registro_instrutor) VALUES (?,?,?,?,?) RETURNING id_instrutor";
                    int id_instrutor;

                    try (PreparedStatement stms = conexao.prepareStatement(sqlInstrutor)){
                        System.out.println("Digite o nome do instrutor:");
                        stms.setString(1, in.nextLine());

                        System.out.println("Digite o email do instrutor:");
                        stms.setString(2, in.nextLine());

                        System.out.println("Digite a senha do instrutor:");
                        stms.setString(3, in.nextLine());

                        System.out.println("informe a descrição do instrutor:");
                        stms.setString(4, in.nextLine());

                        System.out.println("Digite a data de registro (yyyy-MM-dd HH:mm:ss):");
                        String data_registro_instrutor = in.nextLine();
                        stms.setTimestamp(5, java.sql.Timestamp.valueOf(data_registro_instrutor));

                        ResultSet rs = stms.executeQuery();
                        rs.next();
                        id_instrutor = rs.getInt("id_instrutor");
                        System.out.println("Instrutor cadastrado! ID:" + id_instrutor);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;

            case 3: String sqlCurso = "INSERT INTO curso " + 
                    "(nome_curso, descricao_curso, categoria_curso, nivel_curso, duracao, valor_curso, instrutor_curso, data_criacao_curso)" + 
                    "VALUES (?,?,?,?,?,?,?,?) RETURNING id_curso";
                    int id_curso;
                    try (PreparedStatement stms = conexao.prepareStatement(sqlCurso)){
                        System.out.println("Digite o nome do curso:");
                        stms.setString(1, in.nextLine());

                        System.out.println("Informe a descrição do curso:");
                        stms.setString(2, in.nextLine());

                        System.out.println("Digite a categoria do curso:");
                        stms.setString(3, in.nextLine());

                        System.out.println("Digite o nivel do curso:");
                        stms.setString(4, in.nextLine());

                        System.out.println("Digite a duração do curso:");
                        stms.setInt(5, in.nextInt());
                        in.nextLine();

                        System.out.println("Digite o valor do curso:");
                        stms.setBigDecimal(6, in.nextBigDecimal());
                        in.nextLine();

                        System.out.println("Digite o id do instrutor do curso:");
                        stms.setInt(7, in.nextInt());
                        in.nextLine();

                        System.out.println("Digite a data de criação do curso:");
                        String data_criacao_curso = in.nextLine();
                        stms.setTimestamp(8, java.sql.Timestamp.valueOf(data_criacao_curso));

                        ResultSet rs = stms.executeQuery();
                        rs.next();
                        id_curso = rs.getInt("id_curso");
                        System.out.println("Curso cadastrado! ID:" + id_curso);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;

            case 4: String sqlMatricula = "INSERT INTO matricula" +
                    " (aluno_matriculado, curso_matriculado, data_matricula, data_conclusao, status_matricula)" +
                    "VALUES (?,?,?,?,?) RETURNING id_matricula";
                    int id_matricula;
                    try (PreparedStatement stms = conexao.prepareStatement(sqlMatricula)){
                        System.out.println("Digite o ID do aluno:");
                        stms.setInt(1, in.nextInt());
                        in.nextLine();

                        System.out.println("Digite o ID do curso:");
                        stms.setInt(2, in.nextInt());
                        in.nextLine();

                        System.out.println("Digite a data da matricula:");
                        String data_matricula = in.nextLine();
                        stms.setTimestamp(3, java.sql.Timestamp.valueOf(data_matricula));

                        System.out.println("Digite a data de conclusão:");
                        String data_conclusao = in.nextLine();
                        stms.setDate(4, java.sql.Date.valueOf(data_conclusao));

                        System.out.println("Digite a situação:");
                        stms.setString(5, in.nextLine());

                        ResultSet rs = stms.executeQuery();
                        rs.next();
                        id_matricula = rs.getInt("id_matricula");
                        System.out.println("Matricula cadastrada! ID:" + id_matricula);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;

            default: System.out.println("Valor invalido!");
                    break;
            }
        } while (true);

            } catch (Exception e) {
                System.out.println("Falha na conexao, confira os dados inseridos!");
            }
            in.close();
    }
}
