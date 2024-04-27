SELECT 
	T1.view_total 
	, T1.spin_total
	, time
	, memory 
FROM 
	data T1
WHERE 
	T1.view_total > 1 AND T1.spin_total  > 1 AND T1.iter  > 2
ORDER BY 
	T1.view_total

	