/* insert into tb_user (id, email, name, password) values (1, 'breno@gmail.com', 'Breno Duarte', '12345');
insert into tb_user (id, email, name, password) values (2, 'harlon@gmail.com', 'Harlon Garcia', '54321');
insert into tb_user (id, email, name, password) values (3, 'pedro@gmail.com', 'Pedro Arruda', '678910');

insert into tb_challenge (id, description, title, author_id, active) values (1, 'Fazer uma API do banco tal', 'API do banco tal', 1, true);
insert into tb_challenge (id, description, title, author_id, active) values (2, 'Fazer o front end do banco tal', 'Front end do banco tal', 2, true);

insert into tb_solution (id, author_id, challenge_id, repository_url) values (1, 2, 1, 'github.com/harlon/banco-tal');
insert into tb_solution (id, author_id, challenge_id, repository_url) values (2, 3, 1, 'github.com/pedro/banco-tal');

insert into tb_like (participant_id, solution_id) values (1, 1);
insert into tb_like (participant_id, solution_id) values (1, 2);
insert into tb_like (participant_id, solution_id) values (2, 2);
insert into tb_like (participant_id, solution_id) values (3, 2); */