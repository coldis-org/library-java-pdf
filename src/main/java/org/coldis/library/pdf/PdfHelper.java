package org.coldis.library.pdf;

import java.io.IOException;

import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * PDF helper.
 */
public class PdfHelper {

	/**
	 * Reads the text from a PDF.
	 *
	 * @param  pdfReader            The PDF reader.
	 * @param strategy Strategy to read the text from the PDF.
	 * @return                      The text from the PDF.
	 * @throws IntegrationException If there is a problem reading the PDF.
	 */
	public static String readPdfText(final PdfReader pdfReader,final TextExtractionStrategy strategy) throws IntegrationException {
		// PDF text.
		final StringBuffer pdfText = new StringBuffer();
		// Tries to get the PDF text.
		try {
			// For each PDF page.
			for (Integer currentPdfPage = 1; currentPdfPage <= pdfReader.getNumberOfPages(); currentPdfPage++) {
				// Appends the current PDF page text to the complete PDF text.
				pdfText.append(PdfTextExtractor.getTextFromPage(pdfReader, currentPdfPage,strategy));
			}
		}
		// If the text cannot be retrieved.
		catch (final Exception exception) {
			// Throws an PDF read exception.
			throw new IntegrationException(new SimpleMessage("pdf.read.error"), exception);
		}
		// Returns the PDF text.
		return pdfText.toString();
	}

	/**
	 * Reads the text from a PDF.
	 *
	 * @param  pdfContent           The PDF byte content.
	 * @param  strategy             Strategy to read the text from the PDF.
	 * @return                      The text from the PDF.
	 * @throws IntegrationException If there is a problem reading the PDF.
	 */
	public static String readPdfText(final byte[] pdfContent, final TextExtractionStrategy strategy)
			throws IntegrationException {
		// PDF reader.
		PdfReader pdfReader = null;
		// Tries to read the PDF.
		try {
			// Creates the reader for the PDF.
			pdfReader = new PdfReader(pdfContent);
			// Reads and returns the PDF text.
			return PdfHelper.readPdfText(pdfReader, strategy);
		}
		// If the PDF cannot be read.
		catch (final IOException exception) {
			// Throws an PDF read exception.
			throw new IntegrationException(new SimpleMessage("pdf.read.error"), exception);
		}
		// In the end.
		finally {
			// If there is a PDF reader.
			if (pdfReader != null) {
				// Closes the reader.
				pdfReader.close();
			}
		}
	}

}
