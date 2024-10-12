package com.reader.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Data ----> HAY QUE TENER CUIDADO CON ESTA ANOTACION DE LOMBOK EN ENTIDADES:
/*
* [LEER EXPLICACION MAS ABAJO]
* */
@AllArgsConstructor
@NoArgsConstructor
//Esta clase va a mapear los registros del excel a la base de datos
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_generator")
    @SequenceGenerator(name = "my_sequence_generator", sequenceName = "my_sequence", allocationSize = 1)
    //LEER MAS ABAJO EXPLICACION DE ESTA CONFIGURACION
    private Long id;
    private String name;

    @Column(name = "last_name")
    private String lastName;
    private String address;
    private String email;
}

/**
 * La anotación @Data de Lombok es muy útil para generar automáticamente métodos comunes
 * en las clases, como getters, setters, toString(), equals(), y hashCode(). Sin embargo,
 * al trabajar con entidades, especialmente en un contexto de persistencia (como JPA o
 * Spring Data), hay varias consideraciones que debes tener en cuenta.
 */
/**
 * 1. Ciclo de Vida de la Entidad
 * - Equals y HashCode: Si utilizas @Data, se generarán métodos equals() y hashCode().
 *   Si no se implementan adecuadamente, pueden causar problemas en colecciones o al
 *   comparar entidades (por ejemplo, en la comparación de entidades en una base de datos).
 *   Es común que se utilicen solo algunos campos (como el ID) para estos métodos, en
 *   lugar de todos los atributos de la entidad.
 */

/**
 * 2. Lazy Loading y Proxy
 * - Propiedades de Carga Perezosa: Si tienes relaciones entre entidades (como
 *   @OneToMany o @ManyToOne), y usas @Data, puede que se acceda a propiedades que no
 *   estén inicializadas (debido al comportamiento de carga perezosa), lo que puede llevar
 *   a excepciones LazyInitializationException.
 */

/**
 * 3. Mutabilidad
 * - Cambio de Estado: Al generar setters para todos los campos, se permite que el estado
 *   de la entidad cambie en cualquier momento. Esto puede causar problemas si las
 *   entidades se mantienen en el contexto de persistencia, ya que el framework puede
 *   no detectar cambios inesperados.
 */

/**
 * 4. Inmutabilidad
 * - Diseño de Entidades: A veces, es recomendable hacer que las entidades sean
 *   inmutables (por ejemplo, usando final para los campos y sin setters). Esto puede
 *   simplificar el manejo del estado y prevenir cambios no deseados. Lombok ofrece
 *   @Value para crear clases inmutables, pero no puedes combinarlo con @Data.
 */

/**
 * 5. Referencias Cíclicas
 * - toString(): Si tu entidad tiene referencias cíclicas, el método toString()
 *   generado puede entrar en un bucle infinito al intentar imprimir el objeto.
 */

/**
 * Recomendaciones:
 * - Personalizar Equals y HashCode: En lugar de usar @Data, considera implementar
 *   equals() y hashCode() manualmente, utilizando solo los campos que identifican
 *   de manera única la entidad.
 * - Usar @Getter y @Setter Individualmente: Si prefieres tener control sobre qué
 *   campos tienen métodos de acceso, considera usar @Getter y @Setter en lugar de
 *   @Data.
 * - Evitar Setters para Campos Clave: Si trabajas con identificadores de entidades,
 *   podría ser mejor no permitir la modificación del ID después de la creación de la
 *   entidad.
 */
//--------------------------------------------------------------------------------
//CONFIGURACION ID:
/**
 * Una secuencia es un objeto de base de datos que genera un conjunto de números únicos en
 * un orden específico, utilizado principalmente para crear valores de clave primaria.
 *
 * Configuración aplicada:
 * - @Id: Indica que este campo es la clave primaria de la entidad.
 * - @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_generator"):
 *   Define que el valor del campo se generará usando una secuencia.
 *
 *  ------ El atributo 'generator' se utiliza en la anotación @GeneratedValue para especificar
 *  ------ el nombre del generador que se usará para crear valores de clave primaria. En este caso,
 *  ------ se refiere a un generador de secuencias previamente definido mediante @SequenceGenerator.
 *
 *  ------ Ejemplo:
 *  ------ - 'generator = "my_sequence_generator"' indica que se utilizará el generador de secuencias
 *  ------   llamado "my_sequence_generator" para obtener los valores únicos para el campo que
 *  ------   está siendo generado.
 *
 *
 * - @SequenceGenerator(name = "my_sequence_generator", sequenceName = "my_sequence",
 *   allocationSize = 1): Crea una secuencia llamada "my_sequence" y establece que se
 *   asignará un solo número por cada inserción.
 *
 *  ------La anotación @SequenceGenerator se utiliza para definir una secuencia que generará
 *  ------valores únicos, especialmente para claves primarias. Sus atributos son:
 *
 *  ------ - name: El nombre del generador de secuencias, que se usa en la anotación
 *  ------   @GeneratedValue (ejemplo: 'my_sequence_generator').
 *
 *  ------ - sequenceName: El nombre de la secuencia en la base de datos que se utilizará
 *  ------   para generar los valores. Debe coincidir con el nombre de la secuencia definida
 *  ------  en la base de datos (ejemplo: 'my_sequence').
 *
 *  ------ - allocationSize: Define la cantidad de valores que se asignarán en memoria antes
 *  ------   de que se realice una llamada a la secuencia en la base de datos. Un valor de
 *  ------   1 significa que cada vez que se necesite un nuevo valor, se solicitará a la
 *  ------   secuencia. Un valor mayor puede mejorar el rendimiento en inserciones masivas,
 *  ------   pero puede resultar en saltos de números.
 *
 *
 * Diferencias con GenerationType.IDENTITY:
 * - GenerationType.SEQUENCE utiliza una secuencia en la base de datos, lo que es más eficiente
 *   al insertar múltiples registros.
 * - GenerationType.IDENTITY usa una columna auto incrementable, que puede ser menos eficiente
 *   en inserciones masivas, ya que se requiere una consulta adicional para obtener el valor
 *   de la clave primaria.
 */
