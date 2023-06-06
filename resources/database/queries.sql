-- queries

-- resumo
select p.id        as pauta,
       count(a.id) as total_votos
from pauta p
         left join sessao s on s.pauta_id = p.id
         left join voto v on v.pauta_id = p.id
         left join associado a on a.id = v.associado_id
group by p.id
order by p.id;

-- detalhes
select p.id,
       p.titulo,
       s.data_final,
       a.nome
from pauta p
         left join sessao s on s.pauta_id = p.id
         left join voto v on v.pauta_id = p.id
         left join associado a on a.id = v.associado_id
order by p.id;

-- resultado
select v.voto_computado as computado, count(v.id) as total
from voto v
where v.pauta_id = 4
group by v.voto_computado;