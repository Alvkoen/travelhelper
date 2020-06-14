package com.alvcohen.travelhelper.repository;

import com.alvcohen.travelhelper.model.VisaUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaUsageRepository extends JpaRepository<VisaUsage, Long> {

}
