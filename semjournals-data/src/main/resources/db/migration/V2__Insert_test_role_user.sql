INSERT INTO role VALUES ('ea9d4ac3-b5f4-4416-9542-ab8e396510d7', 'user');
INSERT INTO role VALUES ('c582671f-0abe-4165-acb0-851d73954156', 'admin');

INSERT INTO account VALUES ('6270dddf-9945-41eb-baea-3892eca8c063', 'c582671f-0abe-4165-acb0-851d73954156', 'Mario', 'mario@gmail.com', '5a3f8f09e2310f6f81499150ad93d6df06976638f70ebb5334f3a1ff8a800c2c57521859e932544fd5ae3fe6f5b3fffc46bb8f693474c25b6e4ed1a89a2810d0', 'e375cdb0c88408d77047ce1c762acffb6cd82a34f7fa66da1fdad57d770152e1', true);
INSERT INTO account VALUES ('1b94669f-6cf1-4ab5-817e-9ffc7f6ba885', 'ea9d4ac3-b5f4-4416-9542-ab8e396510d7', 'Luigi', 'luigi@gmail.com', 'fb79c22513c2a02cefa800813ef9939e25c3b25ea1018e2b27578b14f7af2c7e0799d2b21e5624fb1bfe58ec5177720249f8a4b79c7fddcc56f79e029044e306', '1d3c5fc4a70480b99c2f7ba077ec500ea7581e4cf6403feb015e176f5bab3d43', false);
INSERT INTO account VALUES ('fa2f9433-1240-4389-9079-94cab6d2b99e', 'c582671f-0abe-4165-acb0-851d73954156', 'Link', 'link@gmail.com', 'fb16ff3e9e82e0d2eb914c4682fe0ae802a77afbdc3101487ab55d27583cb26b446fb29d85358faa91b8de852b5647216ca42ae24b5e210e64a0dae581734ddd', 'b6c05b8472cde618b99a9fc344dc79bd492eacfdc50f929805a72aefe3abcdfe', true);
INSERT INTO account VALUES ('88e9608e-2217-48e5-9dd3-bd830b04e842', 'ea9d4ac3-b5f4-4416-9542-ab8e396510d7', 'Zelda', 'zelda@gmail.com', 'b2f9921b9c5e6c4ae5a3d9cd2c3cf7b9fc7cbf258ac5ac925b8db003d5e6ec43547b0eca7365987f2a44970f292e094629ec19c71dace5cf29d4e7b8bebe50ce', '44a29b83941a00f5f4bb1e6467a5e4d904a87bc9845bc5a2a49f7ee3b2d6e871', false);

INSERT INTO journal VALUES ('a48c772c-34cd-4618-875d-5bdcdc871349', '6270dddf-9945-41eb-baea-3892eca8c063', 'The Mushroom Kingdom History', true);
INSERT INTO journal VALUES ('d405d55d-8b8a-4c8c-8d1c-b65d909a968a', '6270dddf-9945-41eb-baea-3892eca8c063', 'Plumbing 101', false);
INSERT INTO journal VALUES ('e28b4175-60c4-41af-9341-a26e5310e8a2', 'fa2f9433-1240-4389-9079-94cab6d2b99e', 'Hyrule Historia', true);
INSERT INTO journal VALUES ('c750058d-37db-4581-b911-6f9bc3bff0f1', 'fa2f9433-1240-4389-9079-94cab6d2b99e', 'Sword fight for dummies', false);