package service;

import Entity.LineItem;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import JavaBean.TransactionRecordBean;
import Entity.TransactionRecord;
import JavaBean.LineItemBean;
import JavaBean.LineItemTransactionRecord;
import java.sql.SQLException;
import javax.ws.rs.core.UriBuilder;


@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ECommerce
     *
     * @param conten    t representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
 @PUT
    @Path("createECommerceTransactionRecord")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response createECommerceTransactionRecord(@QueryParam("memberID")long memberID, @QueryParam("amountPaid")double payment) {
        try {
           TransactionRecordBean trb = new TransactionRecordBean();  
           TransactionRecord tr = trb.createTransRecord(memberID, payment);
            
            UriBuilder builder = context.getAbsolutePathBuilder();
                    builder.path(Long.toString(tr.getTransactionId()));
            
            return Response.created(builder.build()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    
 @PUT
    @Path("createECommerceLineItemRecord")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response createECommerceLineItemRecord(@QueryParam("salesRecordID")long salesRecordID, @QueryParam("itemEntityID")long itemEntityID, 
            @QueryParam("quantity")int quantity) {
        try {
            LineItemBean lineItemBean = new LineItemBean();   
            
            LineItem lineItem = new LineItem();
            lineItem = lineItemBean.createLineItemRecord(quantity, itemEntityID);
            
            
            LineItemTransactionRecord combined = new LineItemTransactionRecord();
            int result = combined.createSalesRecordLineItem(salesRecordID, lineItem.getId());     
            
            lineItemBean.setItemQuantity(quantity, itemEntityID);   
            
            UriBuilder builder = context.getAbsolutePathBuilder();
            builder.path(Integer.toString(result));
            
            return Response.created(builder.build()).build();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
    
    
    
