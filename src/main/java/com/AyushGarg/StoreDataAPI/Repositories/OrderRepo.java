package com.AyushGarg.StoreDataAPI.Repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.AyushGarg.StoreDataAPI.DTO.analytics.AnalyticsTotalsDTO;
import com.AyushGarg.StoreDataAPI.DTO.analytics.OrdersByDateDTO;
import com.AyushGarg.StoreDataAPI.Models.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long>{

    // Query 1: Fetch the main statistic totals in one go for efficiency.
    @Query("SELECT new com.AyushGarg.StoreDataAPI.DTO.analytics.AnalyticsTotalsDTO(" +
           "  COALESCE(SUM(o.totalPrice), 0.0), " +
           "  COUNT(o.orderId), " +
           "  COUNT(DISTINCT o.customer.customerId)" +
           ") " +
           "FROM Order o " +
           "WHERE o.storeId = :storeId AND o.createdAt BETWEEN :startDate AND :endDate")
    AnalyticsTotalsDTO getAnalyticsTotals(
        @Param("storeId") Long storeId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );

    @Query("SELECT new com.AyushGarg.StoreDataAPI.DTO.analytics.OrdersByDateDTO(" +
           "  o.createdAt as orderDate, " +
           "  COUNT(o.orderId) as orderCount" +
           ") " +
           "FROM Order o " +
           "WHERE o.storeId = :storeId AND o.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY orderDate " +
           "ORDER BY orderDate ASC")
    List<OrdersByDateDTO> findOrdersByDate(
        @Param("storeId") Long storeId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );

    
}
