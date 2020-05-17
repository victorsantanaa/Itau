import numpy as np  
import cv2 as cv
import matplotlib as mpl

b_tamanho = 5
b = []
for i in range(b_tamanho):
    b.append(int(input()))
    
f_tamanho = 31
f = []
for i in range(f_tamanho):
    f.append(int(input()))

g = np.zeros(f_tamanho, dtype="uint8")

# criando função erosão
def erode(funcao, kernel):
    l = 31
    lb = 5
    g = funcao.copy()
    for i in range(31):
        min = funcao[i]
        for j in range(lb):
            i_off = int (i + j - (lb/2)+1)
            if(i_off >= 0 and i_off < l):
                if(min > funcao[i_off] - kernel[j]):
                    min = funcao[i_off] - kernel[j]
        g[i] = min
    return g

def iteracao(funcao, kernel):
    g = funcao
    contador = 0
    while True:
        funcao = g
        g = erode(g, kernel)
        contador += 1
        
        
        if np.array_equal(funcao,g):
            
            break
    return (g, contador)

e = iteracao(f, b)
h = e[0]
for i in range (f_tamanho):
    print(h[i])

print(e[1])
