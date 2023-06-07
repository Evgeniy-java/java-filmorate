package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaaDao;
import ru.yandex.practicum.filmorate.model.Mpaa;

import java.util.Collection;

@RestController
@RequestMapping("/mpaa")
@RequiredArgsConstructor
public class MpaaController {
    private final MpaaDao mpaaDao;

    @GetMapping
    public Collection<Mpaa> getAllMpaa() {
        return mpaaDao.getAllMpaa();
    }

    @GetMapping("/{id}")
    Mpaa getMpaaById(@PathVariable long id) {
        return mpaaDao.getMpaaById(id);
    }
}
