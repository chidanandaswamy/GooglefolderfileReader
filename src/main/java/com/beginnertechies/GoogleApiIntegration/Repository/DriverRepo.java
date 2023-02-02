package com.beginnertechies.GoogleApiIntegration.Repository;

import com.beginnertechies.GoogleApiIntegration.models.Drive1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends JpaRepository<Drive1, Integer> {
}
