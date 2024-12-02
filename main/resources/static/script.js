/*
// Eventos para el monster en el inicio de sesion
   	const monster = document.getElementById('monster');
   	const inputUsuario = document.getElementById('email'); // Cambié 'input-usuario' a 'email'
   	const inputClave = document.getElementById('password'); // Cambié 'input-clave' a 'password'
   	const body = document.querySelector('body');
   	const anchoMitad = window.innerWidth / 2;
   	const altoMitad = window.innerHeight / 2;
   	let seguirPunteroMouse = true;

   	body.addEventListener('mousemove', (m) => {
   		if (seguirPunteroMouse) {
   			if (m.clientX < anchoMitad && m.clientY < altoMitad) {
   				monster.src = "img/idle/2.png";
   				} else if (m.clientX < anchoMitad && m.clientY > altoMitad) {
   					monster.src = "img/idle/3.png";
   					} else if (m.clientX > anchoMitad && m.clientY < altoMitad) {
   						monster.src = "img/idle/5.png";
   						} else {
   							monster.src = "img/idle/4.png";
   							}
   			}
   		})
   		inputUsuario.addEventListener('focus',()=>{
   			seguirPunteroMouse = false;
   			})
   			inputUsuario.addEventListener('blur',()=>{
   				seguirPunteroMouse = true;
   				})
   				inputUsuario.addEventListener('keyup',()=>{
   					let usuario = inputUsuario.value.length;
   					if(usuario >= 0 && usuario<=5){
   						monster.src = 'img/read/1.png';
   					}else if(usuario >= 6 && usuario<=14){
   						monster.src = 'img/read/2.png';
   						}else if(usuario >= 15 && usuario<=20){
   							monster.src = 'img/read/3.png';
   							}else{
   								monster.src = 'img/read/4.png';
   								}
   					})
   					inputClave.addEventListener('focus',()=>{
   						seguirPunteroMouse = false;
   						let cont = 1;
   						const cubrirOjo = setInterval(() => {
   							monster.src = 'img/cover/'+cont+'.png';
   							if(cont < 8){
   								cont++;
   								}else{
   									clearInterval(cubrirOjo);
   									}
   							}, 60);
   						})
   						inputClave.addEventListener('blur',()=>{
   							seguirPunteroMouse = true;
   							let cont = 7;
   							const descubrirOjo = setInterval(() => {
   								monster.src = 'img/cover/'+cont+'.png';
   								if(cont > 1){
   									cont--;
   									}else{
   										clearInterval(descubrirOjo);
   										}
   								}, 60);
   							})
   							
   	let isAuthenticated = false; // Variable para verificar si el usuario está autenticado
       let userId = null; // Variable para almacenar el ID del usuario logueado

       // Cargar eventos al inicio
       document.addEventListener('DOMContentLoaded', function() {
           cargarEventos();
       });
       //Resetear los filtros
       document.getElementById('resetFilters').addEventListener('click', function() {
           // Limpiar los filtros en la interfaz de usuario
           document.getElementById('lugar').value = '';
           document.getElementById('fecha').value = '';
           document.getElementById('categoria').value = '';
           document.getElementById('artista').value = '';

           // Llamar a la función para cargar todos los eventos sin filtros
           cargarEventos(); // Esta función cargará todos los eventos sin aplicar filtros
       });

       function cargarEventos(ciudad = '', fecha = '', categoria = '', artista = '') {
           let url = `http://localhost:8080/api/eventos/listar?ciudad=${ciudad}&fecha=${fecha}&categoria=${categoria}&artista=${artista}`;
           
           fetch(url)
               .then(response => response.json())
               .then(eventos => {
               	console.log('Eventos recibidos:', eventos);
                   const eventosList = document.getElementById('eventosList');
                   eventosList.innerHTML = ''; // Limpiar lista de eventos
                   eventos.forEach(evento => {
                   	console.log('ID del evento:', evento.idEvento);
                       const eventoDiv = document.createElement('div');
                       eventoDiv.classList.add('event');
                       eventoDiv.innerHTML = `
                           <h3>${evento.nombreEvento}</h3>
                           <img src="http://localhost:8080/images/${evento.imagen}" alt="${evento.nombreEvento}">
                           <br /><br />
                           <button onclick="comprarBoleto(${evento.idEvento})">Comprar Boleto</button>
                       `;
                       eventosList.appendChild(eventoDiv);
                   });
               })
               .catch(error => {
                   console.error('Error al cargar eventos:', error);
               });
       }

    // Función para manejar la compra de boletos
       function comprarBoleto(idEvento) {
           if (!isAuthenticated) {
               alert('Por favor, inicie sesión para comprar boletos.');
               return; // Detiene la ejecución si no está autenticado
           }

           let cantidad = prompt('¿Cuántos boletos deseas comprar? (1-5)');
           if (cantidad === null) return; // El usuario canceló

           cantidad = parseInt(cantidad, 10);
           if (isNaN(cantidad) || cantidad < 1 || cantidad > 5) {
               alert('Cantidad no válida. Debe ser un número entre 1 y 5.');
               return;
           }

           // Asegúrate de que userId no sea undefined
           if (userId === null || userId === undefined) {
               alert('Error: ID de usuario no definido.');
               return;
           }

           // Verifica que el idEvento no sea undefined
           if (idEvento === undefined) {
               alert('Error: ID de evento no definido.');
               return;
           }

           // Log para depuración
           console.log('ID de evento:', idEvento);
           console.log('ID de usuario:', userId);
           console.log('Cantidad:', cantidad);

           // Realizar la compra
           fetch(`http://localhost:8080/api/eventos/${idEvento}/comprar?idUsuario=${userId}&cantidad=${cantidad}`, {
               method: 'POST'
           })
           .then(response => {
               if (!response.ok) {
                   throw new Error('Error en la compra: ' + response.statusText);
               }
               return response.text();
           })
           .then(data => {
               alert(data);
           })
           .catch(error => {
               console.error('Error:', error);
               alert('Error al realizar la compra: ' + error.message);
           });
       }


       // Mostrar el modal de inicio de sesión
       document.getElementById('showLoginButton').addEventListener('click', function() {
           document.getElementById('loginModal').style.display = 'block';
       });

       // Mostrar el modal de registro
       document.getElementById('showRegisterButton').addEventListener('click', function() {
           document.getElementById('registerModal').style.display = 'block';
       });

       // Cerrar el modal de inicio de sesión
       document.getElementById('closeLoginModal').addEventListener('click', function() {
           document.getElementById('loginModal').style.display = 'none';
       });

       // Cerrar el modal de registro
       document.getElementById('closeRegisterModal').addEventListener('click', function() {
           document.getElementById('registerModal').style.display = 'none';
       });

       // Manejo del formulario de inicio de sesión
       document.getElementById('loginForm').addEventListener('submit', function(event) {
           event.preventDefault();
           const formData = new FormData(this);
           const data = Object.fromEntries(formData);

           fetch('http://localhost:8080/api/usuarios/login', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/x-www-form-urlencoded'
               },
               body: new URLSearchParams(data)
           })
           .then(response => {
               if (!response.ok) {
                   throw new Error('Error en la autenticación');
               }
               return response.json();
           })
           .then(data => {
               isAuthenticated = true; // Marcar como autenticado
               userId = data.id; // Guardar el ID del usuario
               document.getElementById('userName').innerText = data.nombre; // Asigna el nombre del usuario
               document.getElementById('loginButtonSection').classList.add('hidden'); // Oculta los botones de inicio de sesión y registro
               document.getElementById('userSection').classList.remove('hidden'); // Muestra el nombre del usuario
               alert('¡Bienvenido, ' + data.nombre + '!');
               document.getElementById('loginModal').style.display = 'none';
           })
           .catch(error => {
               console.error('Error:', error);
               alert('Error al iniciar sesión: ' + error.message);
           });
       });

       // Manejo del formulario de registro
       document.getElementById('registroForm').addEventListener('submit', function(event) {
           event.preventDefault();
           const formData = new FormData(this);
           
           const data = Object.fromEntries(formData);
           
           const jsonData = JSON.stringify(data);
           
           fetch('http://localhost:8080/api/usuarios/registrar', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json'
               },
               body: jsonData
           })
           .then(response => {
               if (!response.ok) {
                   throw new Error('Error al registrar');
               }
               alert('Registro exitoso. Puedes iniciar sesión ahora.');
               document.getElementById('registerModal').style.display = 'none';
           })
           .catch(error => {
               console.error('Error:', error);
               alert('Error al registrar: ' + error.message);
           });
       });

       // Manejo del cierre de sesión
       document.getElementById('logoutButton').addEventListener('click', function() {
           fetch('http://localhost:8080/api/usuarios/logout', {
               method: 'POST'
           })
           .then(response => {
               if (!response.ok) {
                   throw new Error('Error al cerrar sesión');
               }
               alert('Sesión cerrada exitosamente');
               
               isAuthenticated = false; // Resetear estado de autenticación
               userId = null; // Resetear ID de usuario

               document.getElementById('loginButtonSection').classList.remove('hidden');
               document.getElementById('userSection').classList.add('hidden'); // Oculta el nombre del usuario
               document.getElementById('userName').innerText = ''; // Limpia el nombre del usuario
           })
           .catch((error) => {
               console.error('Error:', error);
               alert('Error al cerrar sesión: ' + error.message);
           });
       });

       // Cerrar el modal al hacer clic fuera de él
       window.onclick = function(event) {
           const loginModal = document.getElementById('loginModal');
           const registerModal = document.getElementById('registerModal');
           if (event.target === loginModal) {
               loginModal.style.display = 'none';
           }
           if (event.target === registerModal) {
               registerModal.style.display = 'none';
           }
       };
       
       document.addEventListener('DOMContentLoaded', function() {

           document.getElementById('filtrar').addEventListener('click', function() {
               // Obtener los valores de los filtros
               const lugar = document.getElementById('lugar').value;
               const fecha = document.getElementById('fecha').value;
               const categoria = document.getElementById('categoria').value;
               const artista = document.getElementById('artista').value;

               // Crear el objeto con los filtros seleccionados
               const filters = {
                   lugar: lugar,
                   fecha: fecha,
                   categoria: categoria,
                   artista: artista
               };

               // Realizar la solicitud a la API
               fetch('http://localhost:8080/api/eventos/filtrar', {
                   method: 'POST',
                   headers: {
                       'Content-Type': 'application/json'
                   },
                   body: JSON.stringify(filters)
               })
               .then(response => response.json())
               .then(data => {
                   mostrarEventosFiltrados(data);
               })
               .catch(error => {
                   console.error('Error al obtener los eventos filtrados:', error);
               });
           });

           // Función para mostrar los eventos filtrados
           function mostrarEventosFiltrados(eventos) {
               const contenedor = document.getElementById('eventosList');
               contenedor.innerHTML = ''; // Limpiar contenedor

               if (eventos.length === 0) {
                   contenedor.innerHTML = '<p>No se encontraron eventos.</p>';
               } else {
                   eventos.forEach(evento => {
                       const div = document.createElement('div');
                       div.classList.add('evento');
                       div.innerHTML = `
                           <img src="/images/${evento.imagen}" alt="${evento.nombreEvento}" />
                           <h3>${evento.nombreEvento}</h3>
                           <p><strong>Fecha:</strong> ${evento.fecha}</p>
                           <p><strong>Lugar:</strong> ${evento.lugar}</p>
                           <p><strong>Descripción:</strong> ${evento.descripcion}</p>
                           <p><strong>Precio:</strong> $${evento.precioBase}</p> 
                           <button onclick="comprarBoleto(${evento.idEvento})">Comprar Boleto</button>
                       `;
                       contenedor.appendChild(div);
                   });
               }
           }
       });
       
    // Mostrar el botón de "Scroll to top" cuando el usuario haga scroll hacia el footer
       window.onscroll = function() {
           var footer = document.getElementById("footer");
           var scrollToTopButton = document.getElementById("scrollToTopButton");

           // Comprobar si el usuario está cerca del footer
           if (window.innerHeight + window.scrollY >= document.body.offsetHeight - footer.offsetHeight) {
               scrollToTopButton.style.display = "block";  // Mostrar el botón cuando esté cerca del footer
           } else {
               scrollToTopButton.style.display = "none";  // Ocultar el botón cuando no esté cerca
           }
       };

       // Función para mover al usuario hacia la parte superior de la página
       document.getElementById("scrollToTopButton").onclick = function() {
           window.scrollTo({ top: 0, behavior: 'smooth' });
       };
*/
