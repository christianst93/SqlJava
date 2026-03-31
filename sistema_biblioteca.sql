DROP DATABASE IF EXISTS sistema_biblioteca;

CREATE DATABASE sistema_biblioteca;

\c sistema_biblioteca;

CREATE TABLE livro (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) UNIQUE NOT NULL,
    autor VARCHAR(200)  NOT NULL
);

CREATE TABLE exemplar (
    id SERIAL PRIMARY KEY,
    livro_id INTEGER NOT NULL REFERENCES livro (id),
    codigo VARCHAR(7) UNIQUE NOT NULL,
    disponivel BOOLEAN DEFAULT true
);

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(200) UNIQUE NOT NULL
);

CREATE TABLE emprestimo (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES usuario (id),
    exemplar_id INTEGER NOT NULL REFERENCES exemplar (id),
    data_emprestimo DATE DEFAULT CURRENT_DATE,
    data_devolucao DATE
);


-- Inserindo livros
INSERT INTO livro (titulo, autor) VALUES
('Dom Casmurro', 'Machado de Assis'),
('O Hobbit', 'J.R.R. Tolkien'),
('1984', 'George Orwell'),
('A Revolução dos Bichos', 'George Orwell'),
('Capitães da Areia', 'Jorge Amado');

-- Inserindo exemplares (relacionados aos livros)
INSERT INTO exemplar (livro_id, codigo, disponivel) VALUES
(1, 'EX00001', true),
(1, 'EX00002', true),
(2, 'EX00003', true),
(2, 'EX00004', false),
(3, 'EX00005', true),
(4, 'EX00006', true),
(5, 'EX00007', false);

-- Inserindo usuários
INSERT INTO usuario (nome) VALUES
('Ana Silva'),
('Bruno Souza'),
('Carlos Pereira'),
('Daniela Lima');

-- Inserindo empréstimos
INSERT INTO emprestimo (usuario_id, exemplar_id, data_emprestimo, data_devolucao) VALUES
(2, 7, '2026-02-20', '2026-03-05'),
(3, 2, '2026-03-10', '2026-07-07'),
(4, 5, '2026-03-15', '2026-03-25');


SELECT livro.id, livro.titulo, exemplar.id
FROM livro
INNER JOIN exemplar ON (exemplar.livro_id = livro.id)
WHERE exemplar.disponivel = false;

SELECT usuario.id, usuario.nome
FROM usuario
LEFT JOIN emprestimo ON (emprestimo.usuario_id = usuario.id)
WHERE emprestimo.id IS NULL;

SELECT usuario.id, usuario.nome, COUNT (emprestimo.id) AS total_emprestimos
FROM usuario
LEFT JOIN emprestimo ON (emprestimo.usuario_id = usuario.id)
GROUP BY usuario.id, usuario.nome;

SELECT livro.id, livro.titulo, COUNT (exemplar.id) AS total_emprestimos
FROM livro
LEFT JOIN exemplar ON (livro.id = exemplar.livro_id)
GROUP BY livro.id, livro.titulo;

--SELECT COUNT(*) FROM emprestimo;

SELECT livro.id, livro.titulo, exemplar.codigo AS exemplares_disponiveis
FROM livro
INNER JOIN exemplar ON (exemplar.livro_id = livro.id)
WHERE exemplar.disponivel = true
ORDER BY livro.id;


SELECT usuario.id,usuario.nome, COUNT(emprestimo.id) AS total_emprestimos
FROM usuario
LEFT JOIN emprestimo ON emprestimo.usuario_id = usuario.id
GROUP BY usuario.id, usuario.nome
ORDER BY total_emprestimos DESC;

SELECT livro.titulo, COUNT(exemplar.id) as livro_mais_emprestrado
FROM livro
LEFT JOIN exemplar ON (exemplar.livro_id = livro.id)
GROUP BY livro.titulo
ORDER BY livro_mais_emprestrado DESC;

SELECT * 
FROM emprestimo
WHERE data_emprestimo < CURRENT_DATE - INTERVAL '7 days';