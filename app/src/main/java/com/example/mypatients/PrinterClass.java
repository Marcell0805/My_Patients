package com.example.mypatients;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.graphics.pdf.PdfDocument;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class PrinterClass extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public void printDocument(View view, PrintManager printManager, List<String> normalDetails, String[][] tableDetails)
    {
        Context v= view.getContext();
        try
        {
            String jobName = v.getString(R.string.app_name) +
                    " Document";

            printManager.print(jobName, new
                            MyPrintDocumentAdapter(v,normalDetails,tableDetails),
                    null);
        }
        catch (Exception c)
        {
            Toast.makeText(v,"Click event fired", Toast.LENGTH_SHORT).show();
        }

    }
    public class MyPrintDocumentAdapter extends PrintDocumentAdapter
    {
        Context context;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalPages = 1;
        List<String> normalDetails;
        String[][] tableDetails;
        MyPrintDocumentAdapter(Context context, List<String> normalDetails, String[][] tableDetails)
        {
            this.context = context;
            this.normalDetails=normalDetails;
            this.tableDetails=tableDetails;
        }
        @Override
        public void onLayout(PrintAttributes printAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle bundle) {
            myPdfDocument = new PrintedPdfDocument(context, newAttributes);
            pageHeight =
                    newAttributes.getMediaSize().getHeightMils()/1000 * 72;
            pageWidth =
                    newAttributes.getMediaSize().getWidthMils()/1000 * 72;
            if (cancellationSignal.isCanceled() )
            {
                callback.onLayoutCancelled();
                return;
            }
            if (totalPages > 0)
            {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("patient_details.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalPages);

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }

        }

        @Override
        public void onWrite(final PageRange[] pageRanges,
                            ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal,
                            WriteResultCallback callback)
        {
            for (int i = 0; i < totalPages; i++) {
                if (pageInRange(pageRanges, i))
                {
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                            pageHeight, i).create();

                    PdfDocument.Page page =
                            myPdfDocument.startPage(newPage);

                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    drawPage(page, i,normalDetails,tableDetails);
                    myPdfDocument.finishPage(page);
                }
            }

            try {
                myPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }

            callback.onWriteFinished(pageRanges);
        }

    }
    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        for (int i = 0; i<pageRanges.length; i++)
        {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }
    private void drawPage(PdfDocument.Page page,
                          int pagenumber, List<String> normalDetails, String[][] tableDetails)
    {

        String[][] splitL= new String[normalDetails.size()][2];
        normalDetails.toArray(new String[normalDetails.size() * 2]);
        String[]a;
        for(int i=0;i<normalDetails.size();i++)
        {
            String s=normalDetails.get(i);
            a=s.split("-");
            splitL[i][0]=a[0];
            splitL[i][1]=a[1];
        }
        String heading="Patient Details";
        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 40;
        int leftMargin = 15;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText(
                heading,
                leftMargin,
                titleBaseLine,
                paint);
        int k=0;
        int LineSpace=titleBaseLine;
        for(int l=0;l<splitL.length;l++)
        {
            if(k==0)
            {
                LineSpace=LineSpace + 15;
                paint.setTextSize(10);
                canvas.drawText(splitL[l][1], leftMargin, LineSpace, paint);
                paint.setTextSize(8);
                canvas.drawText(splitL[l][0],leftMargin+150,LineSpace,paint);
                k=1;
            }
            else {
                paint.setTextSize(10);
                canvas.drawText(splitL[l][1], leftMargin+350, LineSpace, paint);
                paint.setTextSize(8);
                canvas.drawText(splitL[l][0], leftMargin + 470, LineSpace, paint);
                k=0;
            }
        }
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        //-------------------------------------------------------------------
        //Creation of auto-size table
        //-------------------------------------------------------------------
        /*int yStartPoint=leftMargin;
        int recLength=yStartPoint+500;
        int xStartPoint=LineSpace+30;
        int recWidth=LineSpace+200;*/

        int colCountTotal=tableDetails[0].length;
        int yStartPoint=leftMargin;
        int recLength=0;
        int xStartPoint=LineSpace+30;
        int recWidth=colCountTotal*70;

        //int tableWidth=leftMargin + 500;
        LineSpace=LineSpace+30;



        leftMargin=leftMargin+5;
        LineSpace=LineSpace + 10;
        Typeface plain=Typeface.DEFAULT;
        Typeface bold= Typeface.defaultFromStyle(Typeface.BOLD);
        int colWidth=(recWidth/tableDetails[0].length);
        colWidth=colWidth+20;
        int rows=tableDetails.length;
        int curXstartPoint=LineSpace;
        int nowWidth=0;
        int aaa=0;
        for(int l=0;l<rows;l++)
        {
            recLength=LineSpace;
            paint.setTypeface(bold);
            paint.setTextSize(10);
            canvas.drawText(tableDetails[l][0], leftMargin, LineSpace, paint);
            int cWidth=colWidth;
            int cCount=tableDetails[l].length;
            int drawColCount=1;

            canvas.drawRect(yStartPoint,LineSpace,colWidth,curXstartPoint,paint);
            for(int c=0;c<cCount;c++)
            {
                nowWidth=nowWidth+curXstartPoint;
                paint.setStyle(Paint.Style.STROKE);
                paint.setTextSize(10);
                if(c==0)
                {
                    paint.setTypeface(bold);
                    c++;
                    canvas.drawText(tableDetails[l][c], cWidth*c, LineSpace, paint);
                    //canvas.drawRect(300,xStartPoint+30,60,LineSpace,paint);
                }
                else
                {
                    paint.setTypeface(plain);
                    canvas.drawText(tableDetails[l][c], cWidth*c, LineSpace, paint);

                }

                //canvas.drawRect(how far to the right it goes,lowest point from how far from the top of the page,60,15,paint);
                //canvas.drawRect(colWidth,xStartPoint+30,90,LineSpace,paint);
                //canvas.drawRect(yStartPoint,xStartPoint,recWidth,recLength,paint);
                //canvas.drawRect(yStartPoint+colWidth,xStartPoint,cWidth-15,nowWidth,paint);
                for(int b=1;b<=cCount;b++)
                {
                    canvas.drawRect(colWidth*b,xStartPoint,90,LineSpace,paint);
                    aaa=colWidth*b;
                }
                //cWidth=cWidth+60;
            }
            if(l!=rows)
                LineSpace=LineSpace + 20;

        }
        recWidth=colWidth*(colCountTotal-1);


        canvas.drawRect(yStartPoint,xStartPoint,aaa,recLength,paint);
    }

}
