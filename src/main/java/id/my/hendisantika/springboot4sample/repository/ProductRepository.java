package id.my.hendisantika.springboot4sample.repository;

import id.my.hendisantika.springboot4sample.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot4-sample
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 15/12/25
 * Time: 09.00
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByIsActiveTrue();

    Page<Product> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.isActive = true")
    List<Product> findActiveByCategorY(@Param("category") String category);

    List<Product> findByNameContainingIgnoreCase(String name);
}
