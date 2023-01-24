package dev.bright.web

import org.springframework.data.repository.CrudRepository

interface GreetingsRepository : CrudRepository<Greeting, Int>