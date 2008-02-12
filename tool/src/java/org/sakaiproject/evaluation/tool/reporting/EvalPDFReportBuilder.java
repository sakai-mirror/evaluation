package org.sakaiproject.evaluation.tool.reporting;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import org.jfree.chart.JFreeChart;
import uk.org.ponder.util.UniversalRuntimeException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class EvalPDFReportBuilder {
    private Document document;
    private PdfWriter pdfWriter;
    private MultiColumnText responseArea;
    
    private Font questionTextFont;
    private Font paragraphFont;
    private Font frontTitleFont;
    private Font frontAuthorFont;
    private Font frontInfoFont;
    private Font frontSystemNameFont;
    
    public EvalPDFReportBuilder(OutputStream outputStream) {
        document = new Document();
        try {
            pdfWriter = PdfWriter.getInstance(document, outputStream);
            pdfWriter.setStrictImageSequence(true);
            document.open();
            
            questionTextFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
            paragraphFont = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
            frontTitleFont = new Font(Font.TIMES_ROMAN, 26, Font.NORMAL);
            frontAuthorFont = new Font(Font.TIMES_ROMAN, 18, Font.NORMAL);
            frontInfoFont = new Font(Font.TIMES_ROMAN, 16, Font.NORMAL);
            frontSystemNameFont = new Font(Font.HELVETICA);
        } catch (DocumentException e) {
            throw UniversalRuntimeException.accumulate(e, "Unable to start PDF Report");
        }
    }
    
    public void close() {
        try {
           document.add(responseArea);
           document.close();
        } catch (DocumentException e) {
           throw UniversalRuntimeException.accumulate(e, "Unable to finish PDF Report.");
        }
    }
    
    public void addTitlePage(String evaltitle, String username, String userEid,
            Date startDate, String responseInformation) {
        try {
        
        // Title
        Paragraph titlePara = new Paragraph(evaltitle, frontTitleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);
        
        // User Name
        Paragraph usernamePara = new Paragraph(username + "\n\n", frontAuthorFont);
        usernamePara.setAlignment(Element.ALIGN_CENTER);
        document.add(usernamePara);
        
        // Little info area? I don't know, it was on the mockup though
        Paragraph infoPara = new Paragraph("Results of survey\n\n", frontInfoFont);
        infoPara.setAlignment(Element.ALIGN_CENTER);
        document.add(infoPara);
        
        // Account stuff
        Paragraph accountPara = new Paragraph("Account: " + userEid + " (" + username + "'s Account )\n\n\n", frontInfoFont);
        accountPara.setAlignment(Element.ALIGN_CENTER);
        document.add(accountPara);
        
        // Started on
        Paragraph startedPara = new Paragraph("Started: " + startDate + "\n\n\n", frontInfoFont);
        startedPara.setAlignment(Element.ALIGN_CENTER);
        document.add(startedPara);
        
        // Reply Rate
        Paragraph replyRatePara = new Paragraph(responseInformation + "\n\n\n\n\n\n\n", frontInfoFont);
        replyRatePara.setAlignment(Element.ALIGN_CENTER);
        document.add(replyRatePara);
        
        // Logo and Tagline
        Paragraph productInfoPara = new Paragraph("Camtool Online Evaluation System", frontSystemNameFont);
        productInfoPara.setAlignment(Element.ALIGN_CENTER);
        document.add(productInfoPara);
        
        document.newPage();
        
        responseArea = new MultiColumnText();
        responseArea.addRegularColumns(document.left(), document.right(), 20f, 2);
        } catch (DocumentException de) {
           throw UniversalRuntimeException.accumulate(de, "Unable to create title page");
        }
    }
    
    public void addIntroduction(String title, String text) {
        try {
            this.addQuestionText(title);
            
            //Paragraph textPara = new Paragraph(text);
            //responseArea.addElement(textPara);
            this.addRegularText(text);
        } catch (Exception e) {
            throw UniversalRuntimeException.accumulate(e);
        }
    }
    
    public void addSectionHeader(String headerText) {
       try {
       Paragraph headerPara = new Paragraph(headerText);
       responseArea.addElement(headerPara);
       } catch (Exception e) {
          throw new UniversalRuntimeException("Unable to add Header to PDF Report");
       }
       
    }
    
    public void addEssayResponse(String question, List<String> responses) {
       try {
          //Paragraph questionPara = new Paragraph(question);
          //responseArea.addElement(questionPara);
          this.addQuestionText(question);
          com.lowagie.text.List list = new com.lowagie.text.List(com.lowagie.text.List.UNORDERED);
          for (String essay: responses) {
             com.lowagie.text.ListItem item = new com.lowagie.text.ListItem(essay, this.paragraphFont);
             list.add(item);
          }
          this.responseArea.addElement(list);
       } catch (Exception e) {
          throw new UniversalRuntimeException("Unable to add Essay to PDF Report");
       }
    }
    
    public void addLikertResponse(String question, String[] choices, int[] values, boolean showPercentages) {
        try {
            
        this.addQuestionText(question);
        
        EvalLikertChartBuilder chartBuilder = new EvalLikertChartBuilder();
        chartBuilder.setValues(values);
        chartBuilder.setResponses(choices);
        chartBuilder.setShowPercentages(showPercentages);
        JFreeChart chart = chartBuilder.makeLikertChart();
        
        PdfContentByte cb = pdfWriter.getDirectContent();
        PdfTemplate tp = cb.createTemplate(200, 300);
        Graphics2D g2d = tp.createGraphics(200, 300, new DefaultFontMapper());
        Rectangle2D r2d = new Rectangle2D.Double(0,0,200,300);
        chart.draw(g2d, r2d);
        g2d.dispose();
        Image image = Image.getInstance(tp);
        responseArea.addElement(image);
        
        } catch (Exception e) {
            throw UniversalRuntimeException.accumulate(e);
        }
    }
    
    private void addQuestionText(String question) {
       Paragraph para = new Paragraph(question, questionTextFont);
       try {
         responseArea.addElement(para);
      } catch (DocumentException e) {
         throw UniversalRuntimeException.accumulate(e, "Cannot add question header");
      }
    }
    
    private void addRegularText(String text) {
       Paragraph para = new Paragraph(text, paragraphFont);
       try {
         responseArea.addElement(para);
      } catch (DocumentException e) {
         throw UniversalRuntimeException.accumulate(e, "Cannot add regular text");
      }
    }
}