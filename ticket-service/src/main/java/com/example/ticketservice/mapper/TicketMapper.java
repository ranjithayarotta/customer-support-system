package com.example.ticketservice.mapper;

import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.domain.TicketCommandDTO;
import com.example.ticketservice.domain.TicketQueryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    Ticket toEntity(TicketCommandDTO dto);

    TicketCommandDTO toCommandDto(Ticket entity);

    TicketQueryDTO toQueryDto(Ticket entity);
}
