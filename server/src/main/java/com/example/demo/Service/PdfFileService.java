package com.example.demo.Service;

import com.example.demo.Entity.Employee;
import com.example.demo.Entity.PdfFile;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.Repository.PdfFileRepository;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class PdfFileService {

    @Autowired
    private PdfFileRepository pdfFileRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Value("${app.default-created-by}")
    private Integer defaultCreatedBy;

    public String findById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        return employee.getName();
    }

    public PdfFile savePdf(MultipartFile file) throws IOException {
        PdfFile pdfFile = new PdfFile();
        pdfFile.setName(file.getOriginalFilename());
        pdfFile.setType(file.getContentType());
        pdfFile.setData(file.getBytes());
        return pdfFileRepository.save(pdfFile);
    }

    public Optional<PdfFile> getPdf(Long id) {
        return pdfFileRepository.findById(id);
    }

    public PdfFile addImageToPdf(MultipartFile imageFile, MultipartFile pdfFile, int pageNumber, float x, float y) throws IOException {
        // Load existing PDF
        PdfReader reader = new PdfReader(pdfFile.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        Document document = new Document(pdfDocument); // Ensure pdfDocument is valid

        // Validate page number
        if (pageNumber < 1 || pageNumber > pdfDocument.getNumberOfPages()) {
            throw new IllegalArgumentException("Invalid page number");
        }

        // Load image
        ImageData imageData = ImageDataFactory.create(imageFile.getBytes());
        Image image = new Image(imageData);

        // Get the specified page
        PdfPage page = pdfDocument.getPage(pageNumber);
        PdfCanvas canvas = new PdfCanvas(page);
        //get page size
        Canvas pdfCanvas = new Canvas(canvas, page.getPageSize());

        //set image size
        image.scaleToFit(100,50);

        // Set position
        image.setFixedPosition(pageNumber, x, y);

        //add image to pdf
        pdfCanvas.add(image);

        // close pdf
        document.close();  // Close document first
        pdfDocument.close();  // Close pdfDocument after

        // Save to database
        PdfFile pdfFileEntity = new PdfFile();
        pdfFileEntity.setName(pdfFile.getOriginalFilename());
        pdfFileEntity.setData(outputStream.toByteArray());

        return pdfFileRepository.save(pdfFileEntity);

    }
    public PdfFile addMemoCreatorSign(MultipartFile imageFile, MultipartFile pdfFile) throws IOException {

        String employee = findById(Long.valueOf(defaultCreatedBy));

        // Load existing PDF
        PdfReader reader = new PdfReader(pdfFile.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        Document document = new Document(pdfDocument);

        // Load image
        ImageData imageData = ImageDataFactory.create(imageFile.getBytes());
        Image image = new Image(imageData);
        float imageWidth = 300;
        float imageHeight = 300;

        // Get last page
        int lastPageNumber = pdfDocument.getNumberOfPages();
        PdfPage lastPage = pdfDocument.getPage(lastPageNumber);
        Rectangle pageSize = lastPage.getPageSize();

        int targetPageNumber;

        // Define position for the image (bottom-left corner as an example)
        float x = 50; // Adjust as needed
        float y = 50; // Adjust as needed

        // Check if image fits on last page
        boolean fitsOnLastPage = (y + imageHeight) <= pageSize.getHeight() && (x + imageWidth) <= pageSize.getWidth();

        PdfPage targetPage;
        if (!fitsOnLastPage) {
            // Add a new blank page
            targetPage = pdfDocument.addNewPage();
            targetPageNumber = lastPageNumber+1;
            pageSize = targetPage.getPageSize();
            y = pageSize.getHeight() - imageHeight - 50; // Adjust to fit new page
        } else {
            targetPage = lastPage;
            targetPageNumber = lastPageNumber;
        }

        PdfCanvas canvas = new PdfCanvas(targetPage);
        //get page size
        Canvas pdfCanvas = new Canvas(canvas, targetPage.getPageSize());

        //set image size
        image.scaleToFit(100,50);

        // Set position
        image.setFixedPosition(targetPageNumber,x, y);

        //add image to pdf
        pdfCanvas.add(image);

        Paragraph signText = new Paragraph(employee+" (Creator Signature)")
                .setFontSize(10)
                .setFixedPosition(targetPageNumber, x, y - 15, 150); // y - 15 to place below image
        document.add(signText);

        // Close document
        document.close();
        pdfDocument.close();

        // Save to database
        PdfFile pdfFileEntity = new PdfFile();
        pdfFileEntity.setName(pdfFile.getOriginalFilename());
        pdfFileEntity.setData(outputStream.toByteArray());

        return pdfFileRepository.save(pdfFileEntity);}}
