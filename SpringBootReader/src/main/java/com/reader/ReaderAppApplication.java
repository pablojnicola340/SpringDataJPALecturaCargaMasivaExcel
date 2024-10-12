package com.reader;

import com.monitorjbl.xlsx.StreamingReader;
import com.reader.entity.Customer;
import com.reader.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class ReaderAppApplication implements CommandLineRunner {
    //implements CommandLineRunner: para realizar una ejecucion una vez la app este levantada

    public static void main(String[] args) {
        SpringApplication.run(ReaderAppApplication.class, args);
    }

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {

        long startTimeRead = System.currentTimeMillis();
        log.info("-> Reading file");
        InputStream is = new FileInputStream("../customers.xlsx");
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(500000)//Numero de filas que se van a mantener en memoria.
                .bufferSize(131072) //Tamaño del buffer que se va a asignar para una tarea.
                //REVISAR TABLA DE REFERENCIA MAS ABAJO
                .open(is);

//        List<Customer> customers = new ArrayList<>();
//		for (Sheet sheet : workbook){
//            boolean isHeader = true;
//            for (Row row : sheet) {
//                if (isHeader) {
//                    isHeader = false;
//                    continue;
//                }
//                Customer customer = new Customer();
//                customer.setId((long) row.getCell(0).getNumericCellValue());
//                customer.setName(row.getCell(1).getStringCellValue());
//                customer.setLastName(row.getCell(2).getStringCellValue());
//                customer.setAddress(row.getCell(3).getStringCellValue());
//                customer.setEmail(row.getCell(4).getStringCellValue());
//                customers.add(customer);
//            }
//		}


        List<Customer> customers = StreamSupport.stream(workbook.spliterator(), false)
                .flatMap(sheet -> StreamSupport.stream(sheet.spliterator(), false)
                        .skip(1) //--> se va a saltar los encabezados del fichero
                        .map(con -> {
                            Customer customer = new Customer();
                            customer.setId((long) con.getCell(0).getNumericCellValue());
                            customer.setName(con.getCell(1).getStringCellValue());
                            customer.setLastName(con.getCell(2).getStringCellValue());
                            customer.setAddress(con.getCell(3).getStringCellValue());
                            customer.setEmail(con.getCell(4).getStringCellValue());
                            return customer;
                        }))
                .collect(Collectors.toList());

        long endTimeRead = System.currentTimeMillis();
        log.info("-> Reading finished, time " + (endTimeRead - startTimeRead) + " ms");

        log.info("-> Inserting");



        long startTimeWrite = System.currentTimeMillis();

        //Este saveAll va a armar paquetes de 50.000 registros y los va a insertar.
        //Luego otros 50.000 ...y asi.
        //Esto lo indicamos en la property: spring.jpa.properties.hibernate.jdbc.batch_size
        customerRepository.saveAll(customers);

        long endTimeWrite = System.currentTimeMillis();
        log.info("-> Write finished, time " + (endTimeWrite - startTimeWrite) + " ms");
    }
}

/**
 * Tabla de tamaños de buffer en relación a caracteres, bytes y KB.
 *
 * | **Caracteres** | **Bytes** | **KB** | **Descripción**                                                                                                    |
 * |----------------|-----------|--------|---------------------------------------------------------------------------------------------------------------------|
 * | 512            | 1024      | 1      | **1024 bytes:** Ideal para fragmentos de texto o configuraciones. Uso moderado de memoria, rendimiento óptimo para archivos pequeños. |
 * | 1024           | 2048      | 2      | **2048 bytes:** Tamaño común para archivos de texto cortos. Eficiente en rendimiento y bajo consumo de memoria, adecuado para datos simples. |
 * | 2048           | 4096      | 4      | **4096 bytes:** Adecuado para archivos de texto más grandes o imágenes pequeñas. Mejora el rendimiento al manejar datos agrupados, consumo moderado de memoria. |
 * | 4096           | 8192      | 8      | **8192 bytes:** Común en buffers de aplicaciones de red y bases de datos. Proporciona un buen equilibrio entre rendimiento y uso de memoria. |
 * | 8192           | 16384     | 16     | **16384 bytes:** Utilizado en operaciones de entrada/salida. Mejora la eficiencia de lectura/escritura. Consume más memoria, pero es adecuado para datos intermedios. |
 * | 16384          | 32768     | 32     | **32768 bytes:** Ideal para archivos de tamaño medio, como logs. Buen equilibrio entre rendimiento y consumo de memoria en aplicaciones de tráfico moderado. |
 * | 32768          | 65536     | 64     | **65536 bytes:** Usado en aplicaciones de alto rendimiento. Aumenta la eficiencia de lectura/escritura, pero el consumo de memoria es más alto. |
 * | 65536          | 131072    | 128    | **131072 bytes:** Ideal para archivos grandes o buffers en procesamiento intensivo. Proporciona rendimiento óptimo para grandes volúmenes de datos, con considerable consumo de memoria. |
 */
