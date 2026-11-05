USE kl_club;

-- Fuso de São Paulo (sem horário de verão desde 2019), para CURDATE() bater com o "dia do pedido" da aplicação
SET time_zone = '-03:00';

-- Categorias: cardápio da KD Lanches
INSERT INTO categoria (nome, ativo) VALUES
('Sucos Clássicos', 1),
('Sucos Kd Saúde', 1),
('Vitaminas Kd', 1),
('Shake de Açaí', 1),
('Açaí na Tigela', 1),
('Japonesinhos Kd', 1),
('Tapiocas Salgadas', 1),
('Tapiocas Doces', 1),
('Lanches Prensados', 1),
('Lanches Naturais', 1),
('Especiais', 1),
('Sobremesas', 1);

-- Produtos: recorte do cardápio para demonstração (1 a 3 por categoria).
-- Sucos só no tamanho de 500 ml. O acréscimo de leite dos Sucos Clássicos fica só na descrição.
-- pontuacao = preço arredondado (meio para cima: 9.50 -> 10). imagem = NULL (não há fotos).
-- Indisponíveis, para testar o fluxo: Suco de Cupuaçu, Temaki Empanado, Tapioca de Carne Seca com Queijo.
INSERT INTO produto (fk_categoria, nome, descricao, ingredientes, preco, pontuacao, imagem, disponibilidade) VALUES
(1, 'Suco de Laranja', 'Suco de laranja, 500 ml. Pode ser feito com leite (+ R$ 2,50).', 'Laranja, gelo, açúcar ou adoçante (opcional)', 13.00, 13, NULL, 1),
(1, 'Suco de Maracujá', 'Suco de maracujá, 500 ml. Pode ser feito com leite (+ R$ 2,50).', 'Polpa de maracujá, água, gelo, açúcar ou adoçante (opcional)', 13.00, 13, NULL, 1),
(1, 'Suco de Cupuaçu', 'Suco de cupuaçu, 500 ml. Pode ser feito com leite (+ R$ 2,50).', 'Polpa de cupuaçu, água, gelo, açúcar ou adoçante (opcional)', 13.00, 13, NULL, 0),
(2, 'Suco de Laranja, Cenoura e Gengibre', 'Suco Kd Saúde de laranja, cenoura e gengibre, 500 ml.', 'Suco de laranja, cenoura, gengibre, gelo', 13.00, 13, NULL, 1),
(2, 'Suco de Clorofila', 'Suco Kd Saúde de clorofila, 500 ml.', 'Couve, hortelã, maçã, limão, gengibre, água de coco, gelo', 13.00, 13, NULL, 1),
(3, 'Vitamina Mix I', 'Mamão, morango, banana, maçã, aveia e mel, batidos com laranja ou leite.', 'Mamão, morango, banana, maçã, aveia, mel, suco de laranja ou leite, gelo', 15.00, 15, NULL, 1),
(3, 'Vitamina Mix III', 'Banana, leite, aveia e mel.', 'Banana, leite, aveia, mel, gelo', 12.00, 12, NULL, 1),
(4, 'Shake de Açaí', 'Shake de açaí, 500 ml.', 'Polpa de açaí, leite, banana, xarope de guaraná, gelo', 19.00, 19, NULL, 1),
(5, 'Açaí na Tigela Médio', 'Açaí na tigela com 2 frutas e 3 complementos.', 'Polpa de açaí, xarope de guaraná, 2 frutas à escolha (banana, morango), 3 complementos à escolha (granola, leite condensado, leite em pó, paçoca)', 16.00, 16, NULL, 1),
(6, 'Yakissoba Carne c/ Legumes', 'Yakissoba de carne com legumes.', 'Macarrão para yakissoba, carne bovina em tiras, repolho, cenoura, brócolis, couve-flor, cebola, molho shoyu, óleo de gergelim', 23.00, 23, NULL, 1),
(6, 'Hot Roll Salmão e Kani (12 un)', 'Hot roll de salmão e kani, 12 unidades.', 'Arroz para sushi, alga nori, salmão, kani, cream cheese, farinha panko, molho tarê, cebolinha', 27.00, 27, NULL, 1),
(6, 'Temaki Empanado', 'Temaki empanado.', 'Alga nori, arroz para sushi, salmão, cream cheese, cebolinha, farinha panko, molho tarê', 28.00, 28, NULL, 0),
(7, 'Tapioca de Presunto e Queijo', 'Tapioca salgada de presunto e queijo.', 'Goma de tapioca, presunto, queijo mussarela, manteiga', 10.00, 10, NULL, 1),
(7, 'Tapioca de Frango com Queijo', 'Tapioca salgada de frango com queijo.', 'Goma de tapioca, frango desfiado temperado, queijo mussarela, manteiga', 12.00, 12, NULL, 1),
(7, 'Tapioca de Carne Seca com Queijo', 'Tapioca salgada de carne seca com queijo.', 'Goma de tapioca, carne seca desfiada, cebola, queijo coalho, manteiga', 16.00, 16, NULL, 0),
(8, 'Tapioca de Morango c/ Nutella', 'Tapioca doce de morango com Nutella.', 'Goma de tapioca, morango, Nutella', 15.00, 15, NULL, 1),
(9, 'Misto Quente', 'Lanche prensado de presunto e queijo.', 'Pão de forma, presunto, queijo mussarela, manteiga', 9.00, 9, NULL, 1),
(9, 'Bauru', 'Lanche prensado.', 'Pão francês, presunto, queijo mussarela, tomate, orégano, manteiga', 9.50, 10, NULL, 1),
(10, 'Sanduíche Natural de Salpicão de Frango', 'Sanduíche natural de salpicão de frango.', 'Pão de forma, frango desfiado, cenoura ralada, milho, ervilha, maionese, alface, tomate', 13.50, 14, NULL, 1),
(11, 'Carne Louca', 'Lanche de carne louca.', 'Pão francês, carne bovina desfiada, cebola, pimentão, tomate, alho, cheiro-verde', 14.00, 14, NULL, 1),
(12, 'Salada de Frutas', 'Salada de frutas com mel ou leite condensado.', 'Mamão, banana, maçã, manga, laranja, uva, morango, mel ou leite condensado', 15.00, 15, NULL, 1);

-- Usuários (CPFs com dígitos verificadores válidos)
INSERT INTO usuario (nome, cpf, telefone, conta_criada) VALUES
('Ana Beatriz Souza', '12345678909', '11987654321', '2026-08-02 10:15:00'),
('Carlos Eduardo Lima', '52998224725', '11991234567', '2026-08-15 14:40:00'),
('Fernanda Oliveira', '31847062571', '11976543210', '2026-09-01 09:05:00'),
('Lucas Martins', '74185296355', '11965432198', '2026-09-20 17:30:00'),
('Mariana Costa', '24681357928', '11954321987', '2026-10-05 08:50:00'),
('Rafael Almeida', '13579246828', '21998765432', '2026-10-18 12:20:00');

-- Pedidos
-- valor_total e pontos_gerados batem com os itens; pedidos sem cadastro geram 0 pontos.
-- Status inicial: PIX, CREDITO, DEBITO e VOUCHER nascem PAGO; DINHEIRO nasce PENDENTE.
-- 1 a 7: histórico (datas fixas)
-- 8 a 11 e 15 a 18: hoje (datas relativas, para o dashboard e a gestão não ficarem vazios)
-- 12 a 14: ontem (base de comparação dos KPIs)
-- Os pedidos de hoje usam horários da madrugada para não ficarem no futuro quando o seed roda durante o dia.
-- Nenhum pedido usa produto indisponível.
INSERT INTO pedido (data_hora, modalidade, status_pedido, forma_pagamento, valor_total, pontos_gerados, observacoes, fk_usuario) VALUES
('2026-10-01 08:12:00', 'LOCAL', 'CONCLUIDO', 'PIX', 23.00, 23, NULL, 1),
('2026-10-02 09:30:00', 'RETIRADA', 'CONCLUIDO', 'CREDITO', 40.00, 40, 'Mandar hashi, por favor.', 2),
('2026-10-03 15:45:00', 'LOCAL', 'CONCLUIDO', 'DINHEIRO', 22.00, 0, NULL, NULL),
('2026-10-07 10:05:00', 'RETIRADA', 'CONCLUIDO', 'DEBITO', 31.00, 31, NULL, 3),
('2026-10-10 16:20:00', 'LOCAL', 'CONCLUIDO', 'VOUCHER', 37.00, 37, NULL, 4),
('2026-10-15 12:10:00', 'LOCAL', 'CONCLUIDO', 'PIX', 36.00, 0, 'Mesa 4.', NULL),
('2026-10-22 08:40:00', 'RETIRADA', 'CONCLUIDO', 'PIX', 26.50, 27, NULL, 5),
(TIMESTAMP(CURDATE(), '01:10:00'), 'LOCAL', 'EM_PREPARO', 'CREDITO', 22.50, 23, 'Suco de laranja com leite.', 1),
(TIMESTAMP(CURDATE(), '01:20:00'), 'RETIRADA', 'PRONTO_PARA_RETIRADA', 'DEBITO', 46.00, 46, NULL, 6),
(TIMESTAMP(CURDATE(), '01:35:00'), 'RETIRADA', 'PAGO', 'PIX', 34.00, 34, NULL, 2),
(TIMESTAMP(CURDATE(), '01:40:00'), 'LOCAL', 'PENDENTE', 'DINHEIRO', 18.00, 0, NULL, NULL),
(TIMESTAMP(CURDATE() - INTERVAL 1 DAY, '09:15:00'), 'LOCAL', 'CONCLUIDO', 'CREDITO', 27.00, 27, NULL, 3),
(TIMESTAMP(CURDATE() - INTERVAL 1 DAY, '12:40:00'), 'RETIRADA', 'CONCLUIDO', 'PIX', 36.00, 0, NULL, NULL),
(TIMESTAMP(CURDATE() - INTERVAL 1 DAY, '16:05:00'), 'RETIRADA', 'CANCELADO', 'DEBITO', 40.00, 40, NULL, 4),
(TIMESTAMP(CURDATE(), '00:45:00'), 'LOCAL', 'CONCLUIDO', 'PIX', 15.00, 15, NULL, 5),
(TIMESTAMP(CURDATE(), '00:55:00'), 'RETIRADA', 'CONCLUIDO', 'VOUCHER', 23.00, 0, NULL, NULL),
(TIMESTAMP(CURDATE(), '01:25:00'), 'RETIRADA', 'CANCELADO', 'PIX', 27.00, 27, NULL, 3),
(TIMESTAMP(CURDATE(), '01:45:00'), 'LOCAL', 'PAGO', 'CREDITO', 22.50, 23, NULL, 4);

-- Itens dos pedidos (preco_unitario e pontos_unitario copiados do produto)
INSERT INTO item_pedido (fk_pedido, fk_produto, quantidade, preco_unitario, pontos_unitario) VALUES
(1, 1, 1, 13.00, 13),
(1, 13, 1, 10.00, 10),
(2, 11, 1, 27.00, 27),
(2, 2, 1, 13.00, 13),
(3, 17, 1, 9.00, 9),
(3, 1, 1, 13.00, 13),
(4, 9, 1, 16.00, 16),
(4, 6, 1, 15.00, 15),
(5, 14, 2, 12.00, 12),
(5, 5, 1, 13.00, 13),
(6, 10, 1, 23.00, 23),
(6, 2, 1, 13.00, 13),
(7, 19, 1, 13.50, 14),
(7, 4, 1, 13.00, 13),
(8, 18, 1, 9.50, 10),
(8, 1, 1, 13.00, 13),
(9, 11, 1, 27.00, 27),
(9, 8, 1, 19.00, 19),
(10, 8, 1, 19.00, 19),
(10, 16, 1, 15.00, 15),
(11, 17, 2, 9.00, 9),
(12, 20, 1, 14.00, 14),
(12, 2, 1, 13.00, 13),
(13, 10, 1, 23.00, 23),
(13, 1, 1, 13.00, 13),
(14, 11, 1, 27.00, 27),
(14, 5, 1, 13.00, 13),
(15, 21, 1, 15.00, 15),
(16, 13, 1, 10.00, 10),
(16, 1, 1, 13.00, 13),
(17, 16, 1, 15.00, 15),
(17, 7, 1, 12.00, 12),
(18, 18, 1, 9.50, 10),
(18, 4, 1, 13.00, 13);

-- Funcionários (senha de todos: Senha@123, em texto puro por decisão do projeto)
INSERT INTO funcionario (nome, email, senha, ativo, nivel) VALUES
('Juliana Ribeiro', 'juliana.ribeiro@klclub.com.br', 'Senha@123', 1, 'ADMINISTRADOR'),
('Pedro Henrique Santos', 'pedro.santos@klclub.com.br', 'Senha@123', 1, 'OPERACIONAL'),
('Camila Ferreira', 'camila.ferreira@klclub.com.br', 'Senha@123', 1, 'OPERACIONAL');
