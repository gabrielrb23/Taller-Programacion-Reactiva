document.addEventListener('DOMContentLoaded', function() {
    const formMateria = document.getElementById('form-materia');
    const materiasTableBody = document.getElementById('materias-table-body');

    // Cargar materias al iniciar
    loadMaterias();

    // Manejar envío del formulario
    formMateria.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const materia = {
            nombre: document.getElementById('nombre').value,
            creditos: parseInt(document.getElementById('creditos').value)
        };

        ApiService.createMateria(materia)
            .then(() => {
                formMateria.reset();
                loadMaterias();
            })
            .catch(error => alert('Error: ' + error.message));
    });

    // Función para cargar materias en la tabla
    function loadMaterias() {
        ApiService.getMaterias()
            .then(materias => {
                materiasTableBody.innerHTML = '';
                materias.forEach(materia => {
                    const row = document.createElement('tr');
                    
                    row.innerHTML = `
                        <td>${materia.id}</td>
                        <td>${materia.nombre}</td>
                        <td>${materia.creditos}</td>
                        <td>
                            <button onclick="editMateria(${materia.id})">Editar</button>
                            <button onclick="deleteMateria(${materia.id})">Eliminar</button>
                        </td>
                    `;
                    
                    materiasTableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error cargando materias:', error));
    }

    // Funciones globales para acciones
    window.editMateria = function(id) {
        alert('Editar materia con ID: ' + id);
        // Implementar lógica de edición
    };

    window.deleteMateria = function(id) {
        if (confirm('¿Está seguro de eliminar esta materia?')) {
            alert('Eliminar materia con ID: ' + id);
            // Implementar lógica de eliminación
        }
    };
});